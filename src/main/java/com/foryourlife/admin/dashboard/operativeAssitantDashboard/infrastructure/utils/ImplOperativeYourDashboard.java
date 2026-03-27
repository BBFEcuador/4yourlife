package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementRepository;
import com.foryourlife.admin.crm.statement.domain.StatementStatusEnum;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.DailyStats;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.WeeklyPaymentStats;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.*;
import com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils.TrainingDashboardUtils;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.jline.reader.History;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImplOperativeYourDashboard implements OperativeYourDashboardRepository {
    private final TrainingRepository trainingRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final StatementRepository statementRepository;
    private final History history;

    public ImplOperativeYourDashboard(TrainingRepository trainingRepository, TrainingDashboardUtils trainingDashboardUtils, AttendanceRepository attendanceRepository, PaymentRepository paymentRepository, StatementRepository statementRepository, History history) {
        this.trainingRepository = trainingRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.attendanceRepository = attendanceRepository;
        this.paymentRepository = paymentRepository;
        this.statementRepository = statementRepository;
        this.history = history;
    }

    @Override
    public OperativeYourDashboard findByTrainingId(String trainingId) {
        var training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new BaseException("No se encontró el entrenamiento", List.of("El id " + trainingId + " no corresponde a ningún entrenamiento"))
        );

        if (!training.getCourseLevel().equals(CourseLevel.YOUR)) {
            throw new BaseException("El entrenamiento no es de nivel YOUR", List.of("El entrenamiento con id " + trainingId + " no es de nivel YOUR, por lo tanto no se puede mostrar en el dashboard de operativo asistente"));
        }

        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        var participants = trainingDashboardUtils.loadParticipants(attendances);

        var attendanceDashboard = trainingDashboardUtils.buildGeneralAttendance(attendances, participants);

        var statements = statementRepository.findByTrainingId(trainingId);

        var team = training.getOriginalTeam();

        var trainerName = trainingDashboardUtils.getTrainerName(training, team);

        return new OperativeYourDashboard(
                training.getName(),
                trainerName,
                attendanceDashboard,
                buildOperativeYourPayments(attendances, participants, team)
        );
    }

    public OperativeYourPayments buildOperativeYourPayments(List<Attendance> attendances, Map<String, Participant> participants, Team team) {
        if (attendances.isEmpty()) return null;

        var training = attendances.getFirst().getTraining();

        LocalDate saturday = training.getEndDate().minusDays(1);
        LocalDate sunday = training.getEndDate();

        Training pastTraining = trainingRepository
                .findByNextLevel_Id(training.getId())
                .orElse(null);

        List<Payment> payments = paymentRepository.findAllByParticipantIn(participants.values());

        int previousLifePayments = 0;

        if (pastTraining != null) {
            previousLifePayments = (int) payments.stream()
                    .filter(p -> p.getTraining().getId().equals(pastTraining.getId()))
                    .count();
        }

        int saturdayPayments = (int) payments.stream()
                .filter(p -> p.getCreatedDate().toLocalDate().isEqual(saturday))
                .count();

        int sundayPayments = (int) payments.stream()
                .filter(p -> p.getCreatedDate().toLocalDate().isEqual(sunday))
                .count();

        int totalPayments = saturdayPayments + sundayPayments + previousLifePayments;

        double totalPaymentsPercentage = (double) totalPayments / participants.size() * 100;

        return new OperativeYourPayments(
                previousLifePayments,
                saturdayPayments,
                sundayPayments,
                totalPayments,
                totalPaymentsPercentage,
                buildWeeklyTable(training, team, payments, null)
        );
    }

    public List<YourWeeklyPaymentStats> buildWeeklyTable(Training training, Team team, List<Payment> payments, List<Statement> statements) {

        LocalDate startFriday = training.getStartDate();
        LocalDate nextFriday = training.getNextLevel().getStartDate();

        List<LocalDate> fridayBoundaries = getFridayBoundaries(startFriday, nextFriday);

        Map<LocalDate, List<Payment>> paymentsByDay = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getCreatedDate().toLocalDate()));

        List<YourWeeklyPaymentStats> weeklyStats = new ArrayList<>();

        for (int i = 0; i < fridayBoundaries.size() - 1; i++) {

            LocalDate weekStart = fridayBoundaries.get(i);
            LocalDate weekEnd = fridayBoundaries.get(i + 1);

            YourWeeklyPaymentStats weekStats = new YourWeeklyPaymentStats();
            weekStats.setWeekNumber(i + 1);

            Map<DayOfWeek, YourDailyStats> dayStatsMap = new EnumMap<>(DayOfWeek.class);

            LocalDate date = weekStart;

            while (date.isBefore(weekEnd)) {
                List<Payment> dayPayments = paymentsByDay.getOrDefault(date, List.of());

                YourDailyStats daily = calculateDailyStats(dayPayments, statements);
                dayStatsMap.put(date.getDayOfWeek(), daily);

                date = date.plusDays(1);
            }

            weekStats.setWeeklyPayments(dayStatsMap);
            weeklyStats.add(weekStats);
        }

        return weeklyStats;
    }

    private YourDailyStats calculateDailyStats(List<Payment> payments, List<Statement> statements) {

        YourDailyStats stats = new YourDailyStats();

        var finalPaymentsCount =
                (int) payments.stream().filter(p ->
                        p.getProducts() != null &&
                                p.getProducts().stream().anyMatch(product ->
                                        product.getPrograms() != null &&
                                                product.getPrograms().stream().anyMatch(program ->
                                                        CourseLevel.LIFE.equals(program.getCourseLevel())
                                                )
                                ) &&
                                p.getStatus() == PaymentStatus.COMPLETED
                ).count();

        var agreedPaymentsCount = (int) statements.stream().filter(statement ->
                statement.getStatus().equals(StatementStatusEnum.AGREEMENT)
        ).count();

//        stats.setTotalPaymentsCount();
        return stats;
    }

    private List<LocalDate> getFridayBoundaries(LocalDate start, LocalDate nextFriday) {
        List<LocalDate> fridays = new ArrayList<>();
        LocalDate current = start;

        while (!current.isAfter(nextFriday)) {
            fridays.add(current);
            current = current.plusWeeks(1);
        }

        return fridays;
    }
}
