package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.persistence;

import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.callLogs.domain.CallStatus;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.*;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImplOperativeAssistantDashboardRepository implements OperativeAssistantDashboardRepository {
    private final TrainingRepository trainingRepository;
    private final AttendanceRepository attendanceRepository;
    private final PromiseRepository promiseRepository;
    private final CallRepository callRepository;
    private final PaymentRepository paymentRepository;
    private final TeamRepository teamRepository;

    public ImplOperativeAssistantDashboardRepository(TrainingRepository trainingRepository, AttendanceRepository attendanceRepository, PromiseRepository promiseRepository, CallRepository callRepository, PaymentRepository paymentRepository, TeamRepository teamRepository) {
        this.trainingRepository = trainingRepository;
        this.attendanceRepository = attendanceRepository;
        this.promiseRepository = promiseRepository;
        this.callRepository = callRepository;
        this.paymentRepository = paymentRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public OperativeAssistantDashboard getOpAssistDashboardByTeamId(String teamId) {
        var team = teamRepository.findById(teamId).orElseThrow(
                () -> new BaseException("Team not found", List.of("Team with id " + teamId + " not found"))
        );
        var training = team.getTraining();

        Training focus = null, your = null, life1 = null, life2 = null, life3 = null;

        if (training.getCourseLevel().equals(CourseLevel.YOUR)) {
            focus = training;
        }
        if (training.getCourseLevel().equals(CourseLevel.YOUR)) {
            your = training;
            focus = trainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE)) {
            life1 = training;
            your = trainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            focus = (your != null) ? trainingRepository.findByNextLevel_Id(your.getId()).orElse(null) : null;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_2)) {
            life2 = training;
            life1 = trainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            your = (life1 != null) ? trainingRepository.findByNextLevel_Id(life1.getId()).orElse(null) : null;
            focus = (your != null) ? trainingRepository.findByNextLevel_Id(your.getId()).orElse(null) : null;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_3)) {
            life3 = training;
            life2 = trainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            life1 = (life2 != null) ? trainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
            your = (life1 != null) ? trainingRepository.findByNextLevel_Id(life1.getId()).orElse(null) : null;
            focus = (your != null) ? trainingRepository.findByNextLevel_Id(your.getId()).orElse(null) : null;
        }

        List<TrainingInfo> trainingInfos = new ArrayList<>();
        if (focus != null) trainingInfos.add(buildOperativeAssistantDashboardSection(focus));
        if (your != null) trainingInfos.add(buildOperativeAssistantDashboardSection(your));
        if (life1 != null) trainingInfos.add(buildOperativeAssistantDashboardSection(life1));
        if (life2 != null) trainingInfos.add(buildOperativeAssistantDashboardSection(life2));
        if (life3 != null) trainingInfos.add(buildOperativeAssistantDashboardSection(life3));
        return new OperativeAssistantDashboard(
                trainingInfos
        );
    }

    public TrainingInfo buildOperativeAssistantDashboardSection(Training training) {
        var attendances = attendanceRepository.findAttendanceByTraining(training.getId());

        int participantDeclarations = 0, masterLifesDeclarations = 0, enrolmentsDeclarations;
        if (training.getCourseLevel() != CourseLevel.YOUR) {
            var declarations = promiseRepository.findByTrainingId(training.getId());

            if (!declarations.isEmpty()) {
                for (var declaration : declarations) {
                    var participant = training.getOriginalTeam().getUsers().stream().filter(user -> user.getUser().getId().equals(declaration.getUser().getId())).findFirst();
                    if (participant.isPresent()) {
                        participantDeclarations += declaration.getThirdPromise();
                    } else {
                        var masterLife = training.getOriginalTeam().getMasterLife().stream().filter(ml -> ml.getUser().getId().equals(declaration.getUser().getId())).findFirst();
                        if (masterLife.isPresent()) {
                            masterLifesDeclarations += declaration.getThirdPromise();
                        }
                    }
                }
            }
        }

        enrolmentsDeclarations = participantDeclarations + masterLifesDeclarations;

        List<CallsInfo> callsInfoList = new ArrayList<>();

        for (CallStatus status : CallStatus.values()) {
            CallsInfo info = new CallsInfo();
            info.setStatus(status);
            info.setTotalCalls(0);
            callsInfoList.add(info);
        }

        Map<CallStatus, CallsInfo> map = callsInfoList.stream()
                .collect(Collectors.toMap(CallsInfo::getStatus, ci -> ci));

        callRepository.findAllByTrainingId(training.getId())
                .forEach(call ->
                        call.getCallLogs().forEach(callLog -> {
                            map.get(callLog.getStatus())
                                    .setTotalCalls(map.get(callLog.getStatus()).getTotalCalls() + 1);
                        })
                );

        return new TrainingInfo(
                training.getCourseLevel(),
                training.getName(),
                training.getOriginalTeam().getName(),
                training.getOriginalTeam().getTrainingNumber().toString(),
                training.getOriginalTeam().getUsers().size(),
                (int) attendances.stream().filter(attendance -> attendance.getSundayAttendance() == AttendanceStatus.ASISTIO).count(),
                participantDeclarations,
                training.getOriginalTeam().getMasterLife().size(),
                (int) attendances.stream().filter(attendance -> attendance.getSundayAttendance() == AttendanceStatus.ASISTIO && training.getOriginalTeam().getMasterLife().stream().anyMatch(ml -> ml.getUser().getId().equals(attendance.getUser().getId()))).count(),
                masterLifesDeclarations,
                training.getOriginalTeam().getUsers().size() + training.getOriginalTeam().getMasterLife().size(),
                (int) attendances.stream().filter(attendance -> attendance.getSundayAttendance() == AttendanceStatus.ASISTIO).count(),
                enrolmentsDeclarations,
                callsInfoList,
                buildWeeklyTable(training)
        );
    }

    private long countByCourseLevel(List<Payment> payments, CourseLevel level, boolean completed) {
        return payments.stream()
                .filter(p -> (completed && p.getStatus() == PaymentStatus.COMPLETED)
                        || (!completed && p.getStatus() == PaymentStatus.PENDING))
                .filter(p -> p.getProducts() != null && p.getProducts().stream()
                        .anyMatch(prod -> prod.getPrograms() != null &&
                                prod.getPrograms().stream().anyMatch(prg ->
                                        prg.getCourseLevel() == level
                                )
                        )
                )
                .count();
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

    public List<WeeklyPaymentStats> buildWeeklyTable(Training training) {

        LocalDate startFriday = training.getStartDate();
        LocalDate nextFriday = training.getNextLevel().getStartDate();

        List<LocalDate> fridayBoundaries = getFridayBoundaries(startFriday, nextFriday);

        List<Payment> payments = new java.util.ArrayList<>(List.of());
            training.getOriginalTeam().getUsers().forEach(participant ->
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

}
