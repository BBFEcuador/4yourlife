package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementRepository;
import com.foryourlife.admin.crm.statement.domain.StatementStatusEnum;
import com.foryourlife.admin.crm.statement.domain.StatementStatusHistory;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.focus.*;
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
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImplOperativeFocusDashboardRepository implements OperativeFocusDashboardRepository {
    private final TrainingRepository trainingRepository;
    private final AttendanceRepository attendanceRepository;
    private final TeamRepository teamRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final StatementRepository statementRepository;
    private final PaymentRepository paymentRepository;

    public ImplOperativeFocusDashboardRepository(TrainingRepository trainingRepository, AttendanceRepository attendanceRepository, TeamRepository teamRepository, TrainingDashboardUtils trainingDashboardUtils, StatementRepository statementRepository, PaymentRepository paymentRepository) {
        this.trainingRepository = trainingRepository;
        this.attendanceRepository = attendanceRepository;
        this.teamRepository = teamRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.statementRepository = statementRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public OperativeFocusDashboard getOpAssistDashboardByTrainingId(String trainingId) {
        var training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new BaseException("No se encontró el entrenamiento", List.of("El id " + trainingId + " no corresponde a ningún entrenamiento"))
        );

        if (!training.getCourseLevel().equals(CourseLevel.FOCUS)) {
            throw new BaseException("El entrenamiento no es de nivel Focus", List.of("El entrenamiento con id " + trainingId + " no es de nivel Focus, por lo tanto no se puede mostrar en el dashboard de operativo asistente"));
        }

        var attendances = attendanceRepository.findAttendanceByTraining(trainingId);

        var participants = trainingDashboardUtils.loadParticipants(attendances);


        var team = Optional.ofNullable(training.getOriginalTeam())
                .or(() -> teamRepository.findByTrainingId(training.getId()))
                .orElseThrow(() -> new BaseException(
                        "No existe equipo para el entrenamiento con id: " + training.getId(),
                        List.of("TEAM_NOT_FOUND")
                ));

        var trainerName = trainingDashboardUtils.getTrainerName(training, team);

        List<Statement> statements = statementRepository.findByTrainingId(training.getId());

        var weekendFocusReport = this.buildWeekendFocusReport(attendances, statements, team);

        var operativeFocusPayments = this.buildOperativeFocusPayments(training, team, statements, attendances, participants);

        return new OperativeFocusDashboard(
                training.getName(),
                trainerName,
                weekendFocusReport,
                operativeFocusPayments
        );
    }

    public WeekendFocusReport buildWeekendFocusReport(
            List<Attendance> attendances,
            List<Statement> statements,
            Team team
    ) {

        List<Attendance> safeAttendances = Optional.ofNullable(attendances).orElse(List.of());
        List<Statement> safeStatements = Optional.ofNullable(statements).orElse(List.of());

        int initialParticipantCount = safeAttendances.size();

        int realParticipantCount = (int) safeAttendances.stream()
                .filter(a -> AttendanceStatus.ASISTIO.equals(a.getSundayAttendance()))
                .count();

        int deserterParticipantCount = (int) safeAttendances.stream()
                .filter(a -> AttendanceStatus.NO_ASISTIO.equals(a.getSundayAttendance())
                        || AttendanceStatus.DESERTO.equals(a.getSundayAttendance()))
                .count();

        double deserterParticipantPercentage = initialParticipantCount > 0 ? (double) deserterParticipantCount / initialParticipantCount : 0;

        int statementsCount = (int) safeStatements.stream()
                .filter(s -> s.getStatus() != null && s.getStatus() != StatementStatusEnum.EMPTY)
                .count();

        double statementsPercentage = initialParticipantCount > 0 ? (double) statementsCount / initialParticipantCount : 0;

        int visionariesCount = Optional.ofNullable(team.getVisionaries())
                .map(List::size)
                .orElse(0);

        List<Staff> staffs = Optional.ofNullable(team.getStaffs()).orElse(List.of());

        int captainCount = (int) staffs.stream()
                .filter(staff -> Boolean.TRUE.equals(staff.getIsCaptain()))
                .count();

        int staffCount = staffs.size();

        return new WeekendFocusReport(
                initialParticipantCount,
                realParticipantCount,
                deserterParticipantCount,
                deserterParticipantPercentage,
                statementsCount,
                statementsPercentage,
                visionariesCount,
                captainCount,
                staffCount
        );
    }

    public OperativeFocusPayments buildOperativeFocusPayments(
            Training training,
            Team team,
            List<Statement> statements,
            List<Attendance> attendances,
            Map<String, Participant> participants
    ) {

        List<Attendance> safeAttendances = Optional.ofNullable(attendances).orElse(List.of());
        if (safeAttendances.isEmpty()) return null;

        List<Statement> safeStatements = Optional.ofNullable(statements).orElse(List.of());

        Collection<Participant> safeParticipants = Optional.ofNullable(participants)
                .map(Map::values)
                .orElse(List.of());

        List<Payment> payments = paymentRepository.findAllByParticipantIn(safeParticipants);

        int yourPaymentsCount = 0;
        int yourPlusLifePaymentsCount = 0;
        int pendingPaymentsCount = 0;

        for (Payment p : payments) {

            if (p == null || p.getProducts() == null || p.getCreatedDate().toLocalDate().isBefore(training.getNextLevel().getEndDate()) ) continue;

            Set<CourseLevel> levels = p.getProducts().stream()
                    .filter(prod -> prod.getPrograms() != null)
                    .flatMap(prod -> prod.getPrograms().stream())
                    .map(Program::getCourseLevel)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (p.getStatus() == PaymentStatus.COMPLETED) {

                if (levels.contains(CourseLevel.YOUR) && levels.size() == 1) {
                    yourPaymentsCount++;
                }

                if (levels.contains(CourseLevel.YOUR) && levels.contains(CourseLevel.LIFE)) {
                    yourPlusLifePaymentsCount++;
                }
            }

            if (p.getStatus() == PaymentStatus.PENDING) {
                pendingPaymentsCount++;
            }
        }

        int totalPaymentsCount = yourPaymentsCount + yourPlusLifePaymentsCount;

        int realParticipantCount = (int) safeAttendances.stream()
                .filter(a -> AttendanceStatus.ASISTIO.equals(a.getSundayAttendance()))
                .count();

        double totalPaymentsPercentage = realParticipantCount > 0
                ? Math.round((double) totalPaymentsCount /  realParticipantCount * 100)
                : 0;

        int possibilityPaymentsCount = (int) safeStatements.stream()
                .filter(s -> StatementStatusEnum.POSSIBILITY.equals(s.getStatus()))
                .count();

        return new OperativeFocusPayments(
                yourPaymentsCount,
                yourPlusLifePaymentsCount,
                totalPaymentsCount,
                totalPaymentsPercentage,
                pendingPaymentsCount,
                possibilityPaymentsCount,
                buildFocusWeeklyPaymentStats(training, team, safeStatements, payments)
        );
    }

    private List<FocusWeeklyPaymentStats> buildFocusWeeklyPaymentStats(
            Training training,
            Team team,
            List<Statement> statements,
            List<Payment> payments
    ) {
        LocalDate start = training.getEndDate().plusDays(1);
        LocalDate end = training.getNextLevel().getEndDate();

        List<FocusWeeklyPaymentStats> result = new ArrayList<>();

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

        FocusWeeklyPaymentStats currentWeek = new FocusWeeklyPaymentStats();
        currentWeek.setWeekNumber(weekNumber);

        Map<DayOfWeek, FocusDailyStats> dayStatsMap = new EnumMap<>(DayOfWeek.class);

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

            FocusDailyStats dailyStats = calculateDailyStats(
                    dayPayments,
                    dayStatements,
                    team
            );

            dayStatsMap.put(date.getDayOfWeek(), dailyStats);

            dayCount++;

            if (dayCount == 7 || date.equals(end)) {

                currentWeek.setFocusWeeklyPayments(dayStatsMap);
                result.add(currentWeek);

                weekNumber++;
                currentWeek = new FocusWeeklyPaymentStats();
                currentWeek.setWeekNumber(weekNumber);

                dayStatsMap = new EnumMap<>(DayOfWeek.class);
                dayCount = 0;
            }

            date = date.plusDays(1);
        }

        return result;
    }

    private FocusDailyStats calculateDailyStats(
            List<Payment> payments,
            List<Statement> statements,
            Team team
    ) {

        FocusDailyStats stats = new FocusDailyStats();

        List<Payment> safePayments = Optional.ofNullable(payments).orElse(List.of());
        List<Statement> safeStatements = Optional.ofNullable(statements).orElse(List.of());

        int yourPayments = 0;
        int yourPlusLife = 0;
        int pending = 0;

        for (Payment p : safePayments) {

            if (p == null || p.getProducts() == null) continue;

            Set<CourseLevel> levels = p.getProducts().stream()
                    .filter(prod -> prod.getPrograms() != null)
                    .flatMap(prod -> prod.getPrograms().stream())
                    .map(Program::getCourseLevel)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (p.getStatus() == PaymentStatus.COMPLETED) {

                if (levels.contains(CourseLevel.YOUR) && !levels.contains(CourseLevel.LIFE)) {
                    yourPayments++;
                }

                if (levels.contains(CourseLevel.LIFE)) {
                    yourPlusLife++;
                }
            }

            if (p.getStatus() == PaymentStatus.PENDING) {
                pending++;
            }
        }

        int possible = (int) safeStatements.stream()
                .filter(s -> getLastStatus(s) == StatementStatusEnum.POSSIBILITY)
                .count();

        int agreed = (int) safeStatements.stream()
                .filter(s -> getLastStatus(s) == StatementStatusEnum.AGREEMENT)
                .count();

        int notInterested = (int) safeStatements.stream()
                .filter(s -> {
                    var status = getLastStatus(s);
                    return status == StatementStatusEnum.NO_INTERESTED
                            || status == StatementStatusEnum.EMPTY;
                })
                .count();

        stats.setYourPaymentsCount(yourPayments);
        stats.setYourPlusLifePaymentsCount(yourPlusLife);
        stats.setTotalPaymentsCount(yourPayments + yourPlusLife);
        stats.setPendingPaymentsCount(pending);
        stats.setAgreedPaymentsCount(agreed);
        stats.setPossiblePaymentsCount(possible);
        stats.setNotInterestPaymentsCount(notInterested);

        int teamSize = Optional.ofNullable(team.getUsers()).map(List::size).orElse(0);

        if (teamSize == 0) {
            stats.setPassPercentage(0);
            stats.setProjectedPercentage(0);
        } else {
            stats.setPassPercentage(stats.getTotalPaymentsCount() / (double) teamSize);
            stats.setProjectedPercentage(
                    ((double) teamSize - stats.getNotInterestPaymentsCount()) / teamSize
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
