package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementRepository;
import com.foryourlife.admin.crm.statement.domain.StatementStatusEnum;
import com.foryourlife.admin.crm.statement.domain.StatementStatusHistory;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.your.*;
import com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils.TrainingDashboardUtils;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.admin.sales.programs.domain.Program;
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

        int realParticipants = (int) attendances.stream().filter(
                attendance -> attendance.getSundayAttendance() != null && attendance.getSundayAttendance().equals(AttendanceStatus.ASISTIO)
        ).count();

        double previousPaymentsPercentage = participants.isEmpty() ? 0 : (double) previousLifePayments / participants.size() * 100;

        double saturdayPaymentsPercentage = realParticipants > 0 ?
                (double) (saturdayPayments + previousLifePayments) / realParticipants * 100
                : 0;

        double sundayPaymentsPercentage = realParticipants > 0 ?
                (double) (sundayPayments + saturdayPayments + previousLifePayments) / realParticipants * 100
                : 0;

        return new OperativeYourPayments(
                previousLifePayments,
                previousPaymentsPercentage,
                saturdayPayments,
                saturdayPaymentsPercentage,
                sundayPayments,
                sundayPaymentsPercentage,
                totalPayments,
                totalPaymentsPercentage,
                buildWeeklyTable(training, team, payments, statements)
        );
    }

    public List<YourWeeklyPaymentStats> buildWeeklyTable(
            Training training,
            Team team,
            List<Payment> payments,
            List<Statement> statements
    ) {

        LocalDate start = training.getStartDate();
        LocalDate end = training.getNextLevel().getStartDate();

        List<YourWeeklyPaymentStats> result = new ArrayList<>();

        Map<LocalDate, List<Payment>> paymentsByDay = payments.stream()
                .collect(Collectors.groupingBy(p -> p.getCreatedDate().toLocalDate()));

        Map<LocalDate, List<Statement>> statementsByDay = statements.stream()
                .flatMap(statement -> {

                    List<StatementStatusHistory> history =
                            Optional.ofNullable(statement.getStatementStatusHistory())
                                    .orElse(List.of());

                    return history.stream()
                            .filter(h -> h.getStatus() == StatementStatusEnum.AGREEMENT)
                            .map(h -> Map.entry(h.getChangedAt().toLocalDate(), statement));
                })
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        LocalDate date = start;
        int weekNumber = 1;

        YourWeeklyPaymentStats currentWeek = new YourWeeklyPaymentStats();
        currentWeek.setWeekNumber(weekNumber);

        Map<DayOfWeek, YourDailyStats> dayStatsMap = new EnumMap<>(DayOfWeek.class);

        int dayCount = 0;

        while (!date.isAfter(end)) {

            LocalDate currentDate = date;

            List<Payment> dayPayments = paymentsByDay.entrySet().stream()
                    .filter(e -> !e.getKey().isAfter(currentDate))
                    .flatMap(e -> e.getValue().stream())
                    .toList();

            List<Statement> dayStatements = statementsByDay.entrySet().stream()
                    .filter(e -> !e.getKey().isAfter(currentDate))
                    .flatMap(e -> e.getValue().stream())
                    .toList();

            YourDailyStats dailyStats = calculateDailyStats(
                    dayPayments,
                    dayStatements,
                    team
            );

            dayStatsMap.put(date.getDayOfWeek(), dailyStats);

            dayCount++;

            if (dayCount == 7 || date.equals(end)) {

                currentWeek.setWeeklyPayments(dayStatsMap);
                result.add(currentWeek);

                weekNumber++;
                currentWeek = new YourWeeklyPaymentStats();
                currentWeek.setWeekNumber(weekNumber);

                dayStatsMap = new EnumMap<>(DayOfWeek.class);
                dayCount = 0;
            }

            date = date.plusDays(1);
        }

        return result;
    }

    private YourDailyStats calculateDailyStats(
            List<Payment> payments,
            List<Statement> statements,
            Team team
    ) {

        YourDailyStats stats = new YourDailyStats();

        List<Payment> safePayments = Optional.ofNullable(payments).orElse(List.of());
        List<Statement> safeStatements = Optional.ofNullable(statements).orElse(List.of());

        int finalPaymentsCount = 0;

        for (Payment p : safePayments) {

            if (p == null || p.getProducts() == null) continue;

            boolean hasLife = p.getProducts().stream()
                    .filter(prod -> prod.getPrograms() != null)
                    .flatMap(prod -> prod.getPrograms().stream())
                    .map(Program::getCourseLevel)
                    .anyMatch(CourseLevel.LIFE::equals);

            if (p.getStatus() == PaymentStatus.COMPLETED && hasLife) {
                finalPaymentsCount++;
            }
        }

        int agreedPaymentsCount = (int) safeStatements.stream()
                .filter(s -> getLastStatus(s) == StatementStatusEnum.AGREEMENT)
                .count();

        stats.setAgreedPaymentsCount(agreedPaymentsCount);

        int finalCount = Math.max(0, finalPaymentsCount - agreedPaymentsCount);
        stats.setFinalPaymentsCount(finalCount);

        stats.setTotalPaymentsCount(
                stats.getAgreedPaymentsCount() + stats.getFinalPaymentsCount()
        );

        int teamSize = Optional.ofNullable(team.getUsers()).map(List::size).orElse(0);

        if (teamSize == 0) {
            stats.setPassPaymentsPercentage(0);
            stats.setProjectedPaymentsPercentage(0);
        } else {
            stats.setPassPaymentsPercentage(
                    stats.getFinalPaymentsCount() / (double) teamSize
            );
            stats.setProjectedPaymentsPercentage(
                    (stats.getFinalPaymentsCount() + stats.getAgreedPaymentsCount()) / (double) teamSize
            );
        }

        return stats;
    }

    private StatementStatusEnum getLastStatus(Statement s) {
        return Optional.ofNullable(s.getStatementStatusHistory())
                .orElse(List.of())
                .stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparing(StatementStatusHistory::getChangedAt))
                .map(StatementStatusHistory::getStatus)
                .orElse(StatementStatusEnum.EMPTY);
    }
}
