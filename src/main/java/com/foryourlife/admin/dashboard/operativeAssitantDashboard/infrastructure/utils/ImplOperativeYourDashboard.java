package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.crm.statement.domain.StatementRepository;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.DailyStats;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.WeeklyPaymentStats;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.OperativeYourDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.OperativeYourDashboardRepository;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.OperativeYourPayments;
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

    public ImplOperativeYourDashboard(TrainingRepository trainingRepository, TrainingDashboardUtils trainingDashboardUtils, AttendanceRepository attendanceRepository, PaymentRepository paymentRepository, StatementRepository statementRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.attendanceRepository = attendanceRepository;
        this.paymentRepository = paymentRepository;
        this.statementRepository = statementRepository;
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
                buildOperativeYourPayments(attendances, participants)
        );
    }

    public OperativeYourPayments buildOperativeYourPayments(List<Attendance> attendances, Map<String, Participant> participants) {
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
                null
        );
    }

    public List<WeeklyPaymentStats> buildWeeklyTable(Training training, Team team) {

        LocalDate startFriday = training.getStartDate();
        LocalDate nextFriday = training.getNextLevel().getStartDate();

        List<LocalDate> fridayBoundaries = getFridayBoundaries(startFriday, nextFriday);

        List<Payment> payments = new java.util.ArrayList<>(List.of());
        team.getUsers().forEach(participant ->
                paymentRepository.findAllBetweenDates(training.getStartDate().atStartOfDay(), training.getNextLevel().getStartDate().atStartOfDay()).forEach(
                        payment -> {
                            if (payment.getParticipant().getId().equals(participant.getId())) {
                                payments.add(payment);
                            }
                        }
                )
        );

        Map<LocalDate, List<Payment>> paymentsByDay = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getCreatedDate().toLocalDate()));

        List<WeeklyPaymentStats> weeklyStats = new ArrayList<>();

        for (int i = 0; i < fridayBoundaries.size() - 1; i++) {

            LocalDate weekStart = fridayBoundaries.get(i);
            LocalDate weekEnd = fridayBoundaries.get(i + 1); // exclusivo

            WeeklyPaymentStats weekStats = new WeeklyPaymentStats();
            weekStats.setWeekNumber(i + 1);

            Map<DayOfWeek, DailyStats> dayStatsMap = new EnumMap<>(DayOfWeek.class);

            LocalDate date = weekStart;

            while (date.isBefore(weekEnd)) {

                List<Payment> dayPayments = paymentsByDay.getOrDefault(date, List.of());

                DailyStats daily = calculateDailyStats(dayPayments);
                dayStatsMap.put(date.getDayOfWeek(), daily);

                date = date.plusDays(1);
            }

            weekStats.setStatsPerDay(dayStatsMap);
            weeklyStats.add(weekStats);
        }

        return weeklyStats;
    }

    private DailyStats calculateDailyStats(List<Payment> payments) {

        DailyStats stats = new DailyStats();

        stats.setParticipantsFinal(
                (int) payments.stream()
                        .map(p -> p.getParticipant().getId())
                        .distinct()
                        .count()
        );

        stats.setYourCount(
                (int) payments.stream()
                        .filter(p -> p.getParticipant().getParticipantLevel().getCourseLevel() == CourseLevel.YOUR)
                        .count()
        );

        stats.setYourLifeCount(
                (int) payments.stream()
                        .filter(p -> {
                            var level = p.getParticipant().getParticipantLevel().getCourseLevel();
                            return level == CourseLevel.YOUR || level == CourseLevel.LIFE;
                        })
                        .count()
        );

        stats.setTotalPayments(payments.size());

        stats.setPartialPayments(
                (int) payments.stream()
                        .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                        .count()
        );

        if (stats.getParticipantsFinal() > 0) {
            stats.setPassPercent(
                    (stats.getYourLifeCount() * 100.0) / stats.getParticipantsFinal()
            );
        } else {
            stats.setPassPercent(0.0);
        }

        if (stats.getParticipantsFinal() > 0) {
            stats.setProjectedPercent(
                    ((stats.getYourLifeCount() + stats.getPartialPayments()) * 100.0)
                            / stats.getParticipantsFinal()
            );
        } else {
            stats.setProjectedPercent(0.0);
        }

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
