package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementRepository;
import com.foryourlife.admin.crm.statement.domain.StatementStatusEnum;
import com.foryourlife.admin.crm.statement.domain.StatementStatusHistory;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.*;
import com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils.TrainingDashboardUtils;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.staff.domain.Staff;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImplOperativeYourDashboard implements OperativeYourDashboardRepository {
    private final TrainingRepository trainingRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final StatementRepository statementRepository;
    private final TeamRepository teamRepository;

    public ImplOperativeYourDashboard(TrainingRepository trainingRepository, TrainingDashboardUtils trainingDashboardUtils, AttendanceRepository attendanceRepository, PaymentRepository paymentRepository, StatementRepository statementRepository, TeamRepository teamRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.attendanceRepository = attendanceRepository;
        this.paymentRepository = paymentRepository;
        this.statementRepository = statementRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    @Transactional
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

        var team = Optional.ofNullable(training.getOriginalTeam())
                .or(() -> teamRepository.findByTrainingId(training.getId()))
                .orElseThrow(() -> new BaseException(
                        "No existe equipo para el entrenamiento con id: " + training.getId(),
                        List.of("TEAM_NOT_FOUND")
                ));

        var trainerName = trainingDashboardUtils.getTrainerName(training, team);

        int captainCount = 0;
        int staffCount = 0;
        if (team.getStaffs() != null) {
            captainCount = Math.toIntExact(team.getStaffs().stream()
                    .filter(Staff::getIsCaptain).count());

            staffCount = team.getStaffs().size() - captainCount;
        }

        return new OperativeYourDashboard(
                training.getName(),
                trainerName,
                captainCount,
                staffCount,
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

        List<Statement> statements = statementRepository.findByTrainingId(training.getId());

        return new OperativeYourPayments(
                previousLifePayments,
                saturdayPayments,
                sundayPayments,
                totalPayments,
                totalPaymentsPercentage,
                buildWeeklyTable(training, team, payments, statements)
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

            Map<LocalDate, List<Statement>> statementsByDay = statements.stream()
                    .flatMap(statement -> {

                        List<StatementStatusHistory> history =
                                Optional.ofNullable(statement.getStatementStatusHistory())
                                        .orElse(List.of());

                        return history.stream()
                                .filter(h -> h.getStatus() == StatementStatusEnum.AGREEMENT)
                                .map(h -> Map.entry(h.getChangedAt().toLocalDate(), statement));
                    })
                    .collect(Collectors.groupingBy(Map.Entry::getKey,
                            Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
            while (date.isBefore(weekEnd)) {
                List<Payment> dayPayments = paymentsByDay.getOrDefault(date, List.of());
                LocalDate finalDate = date;
                List<Statement> dayStatements = statementsByDay.entrySet().stream()
                        .filter(entry -> !entry.getKey().isAfter(finalDate))
                        .flatMap(entry -> entry.getValue().stream())
                        .toList();
                YourDailyStats daily = calculateDailyStats(dayPayments, dayStatements, team);
                dayStatsMap.put(date.getDayOfWeek(), daily);

                date = date.plusDays(1);
            }

            weekStats.setWeeklyPayments(dayStatsMap);
            weeklyStats.add(weekStats);
        }

        return weeklyStats;
    }

    private YourDailyStats calculateDailyStats(
            List<Payment> payments,
            List<Statement> statements,
            Team team
    ) {
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

        var agreedPaymentsCount = (int) statements.stream()
                .filter(statement -> {

                    List<StatementStatusHistory> history =
                            Optional.ofNullable(statement.getStatementStatusHistory())
                                    .orElse(List.of());

                    return history.stream()
                            .anyMatch(h -> h.getStatus() == StatementStatusEnum.AGREEMENT);
                })
                .count();

        stats.setAgreedPaymentsCount(agreedPaymentsCount);
        int finalCount = Math.max(0, finalPaymentsCount - agreedPaymentsCount);
        stats.setFinalPaymentsCount(finalCount);
        stats.setTotalPaymentsCount(stats.getAgreedPaymentsCount() + stats.getFinalPaymentsCount());
        int teamSize = team.getUsers() != null ? team.getUsers().size() : 0;

        if (teamSize == 0) {
            stats.setPassPaymentsPercentage(0);
            stats.setProjectedPaymentsPercentage(0);
        } else {
            stats.setPassPaymentsPercentage(stats.getFinalPaymentsCount() / (double) teamSize);
            stats.setProjectedPaymentsPercentage(
                    (stats.getFinalPaymentsCount() + stats.getAgreedPaymentsCount()) / (double) teamSize
            );
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
