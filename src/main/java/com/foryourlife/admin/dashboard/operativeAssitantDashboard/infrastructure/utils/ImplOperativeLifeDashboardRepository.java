package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.callLogs.domain.CallLog;
import com.foryourlife.admin.crm.callLogs.domain.CallStatus;
import com.foryourlife.admin.crm.callLogs.domain.CallType;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life.*;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.life.DeclarationStats;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.life.LifeAttendanceDashboard;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.life.UserDashboardDto;
import com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils.TrainerLifeViewRepositoryImpl;
import com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils.TrainingDashboardUtils;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.programs.training.infrastructure.JPATrainingRepository;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImplOperativeLifeDashboardRepository implements OperativeLifeDashboardRepository {
    private final TrainingRepository trainingRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final AttendanceRepository attendanceRepository;
    private final PromiseRepository promiseRepository;
    private final JPATrainingRepository jPATrainingRepository;
    private final CallRepository callRepository;
    private final TeamRepository teamRepository;
    private final TrainerLifeViewRepositoryImpl trainerLifeViewRepositoryImpl;

    public ImplOperativeLifeDashboardRepository(TrainingRepository trainingRepository, TrainingDashboardUtils trainingDashboardUtils, AttendanceRepository attendanceRepository, PromiseRepository promiseRepository, JPATrainingRepository jPATrainingRepository, CallRepository callRepository, TeamRepository teamRepository, TrainerLifeViewRepositoryImpl trainerLifeViewRepositoryImpl) {
        this.trainingRepository = trainingRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.attendanceRepository = attendanceRepository;
        this.promiseRepository = promiseRepository;
        this.jPATrainingRepository = jPATrainingRepository;
        this.callRepository = callRepository;
        this.teamRepository = teamRepository;
        this.trainerLifeViewRepositoryImpl = trainerLifeViewRepositoryImpl;
    }

    @Override
    @Transactional
    public List<OperativeLifeDashboard> getOperativeLifeDashboard(String trainingId) {
        var training = trainingRepository.findById(trainingId).orElseThrow(() -> new RuntimeException("Training not found"));

        Training life1 = null, life2 = null, life3 = null, life4 = null;

        if (training.getCourseLevel().equals(CourseLevel.LIFE)) {
            life1 = training;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_2)) {
            life2 = training;
            life1 = jPATrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_3)) {
            life3 = training;
            life2 = jPATrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            life1 = (life2 != null) ? jPATrainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_GRADUATE)) {
            life4 = training;
            life3 = jPATrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            life2 = (life3 != null) ? jPATrainingRepository.findByNextLevel_Id(life3.getId()).orElse(null) : null;
            life1 = (life2 != null) ? jPATrainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
        }

        List<OperativeLifeDashboard> dashboards = new ArrayList<>();
        if (life1 != null) dashboards.add(buildOperativeLifeDashboard(life1));
        if (life2 != null) dashboards.add(buildOperativeLifeDashboard(life2));
        if (life3 != null) dashboards.add(buildOperativeLifeDashboard(life3));
        if (life4 != null) dashboards.add(buildOperativeLifeDashboard(life4));
        return dashboards;
    }

    public OperativeLifeDashboard buildOperativeLifeDashboard(Training training) {
        var callInfoList = this.buildCallsTable(training);

        var team = Optional.ofNullable(training.getOriginalTeam())
                .or(() -> teamRepository.findByTrainingId(training.getId()))
                .orElseThrow(() -> new BaseException(
                        "No existe equipo para el entrenamiento con id: " + training.getId(),
                        List.of("TEAM_NOT_FOUND")
                ));

        var trainerName = trainingDashboardUtils.getTrainerName(training, team);

        var weekendReport = BuildWeekendReport(training, team);

        return new OperativeLifeDashboard(
                training.getName(),
                trainerName,
                training.getCourseLevelDisplay(),
                weekendReport,
                callInfoList
        );
    }

    public WeekendLifeReport BuildWeekendReport(Training training, Team team) {
        var attendances = attendanceRepository.findAttendanceByTraining(training.getId());

        var participants = trainingDashboardUtils.loadParticipants(attendances);


        List<Promise> promises = promiseRepository.findByTrainingId(training.getId());

        Map<String, Promise> promiseMap = promises.stream()
                .filter(p -> p.getUser() != null)
                .collect(Collectors.toMap(p -> p.getUser().getId(), p -> p, (a, b) -> a));

        List<UserDashboardDto> userDashboards = attendances.stream()
                .filter(a -> a.getUser() != null)
                .map(a -> {
                    User user = a.getUser();
                    String entity = trainingDashboardUtils.resolveUserType(user);
                    Promise userPromise = promiseMap.get(a.getUser().getId());
                    return new UserDashboardDto(
                            user.getName(),
                            entity,
                            a.getFridayAttendance(),
                            a.getSaturdayAttendance(),
                            a.getSundayAttendance(),
                            userPromise != null ? userPromise.getFirstPromise() : 0,
                            userPromise != null ? userPromise.getSecondPromise() : 0,
                            userPromise != null ? userPromise.getThirdPromise() : 0,
                            userPromise != null ? userPromise.getAchievedCount() : 0,
                            userPromise != null ? userPromise.getPaidCount() : 0
                    );
                }).toList();

        LifeAttendanceDashboard lifeAttendanceDashboard = trainerLifeViewRepositoryImpl.buildAttendanceDashboard(attendances, participants, team.getMasterLife(), userDashboards);

        DeclarationStats declarationStats = trainerLifeViewRepositoryImpl.buildDeclarationStats(userDashboards);

        double enrollmentIndex = declarationStats.getTotalTeamLifePromisesCount() > 0
                ? Math.round(((double) lifeAttendanceDashboard.getTotalAttendancesCount() / declarationStats.getTotalTeamLifePromisesCount()) * 100.0) / 100.0
                : 0.0;

        double realEnrollmentIndex = declarationStats.getTotalTeamLifePromisesCount() > 0
                ? Math.round(((double) declarationStats.getTotalUsersEnrollersCount() / declarationStats.getTotalTeamLifePromisesCount()) * 100.0) / 100.0
                : 0.0;

        return new WeekendLifeReport(
                lifeAttendanceDashboard.getTotalParticipants(),
                lifeAttendanceDashboard.getParticipantAttendancesCount(),
                declarationStats.getTotalParticipantPromisesCount(),
                lifeAttendanceDashboard.getTotalMasterParticipants(),
                lifeAttendanceDashboard.getMasterAttendancesCount(),
                declarationStats.getTotalMasterLifePromisesCount(),
                lifeAttendanceDashboard.getTotalTotalUsers(),
                lifeAttendanceDashboard.getTotalAttendancesCount(),
                declarationStats.getTotalTeamLifePromisesCount(),
                declarationStats.getTotalUsersEnrollersCount(),
                enrollmentIndex,
                realEnrollmentIndex
        );
    }

    public List<CallTypeStats> buildCallsTable(Training training) {
        Map<CallType, Map<CallStatus, CallsInfo>> callsByType = new HashMap<>();

        for (CallType type : CallType.values()) {

            Map<CallStatus, CallsInfo> statusMap = new HashMap<>();

            for (CallStatus status : CallStatus.values()) {
                CallsInfo info = new CallsInfo();
                info.setStatus(status);
                info.setTotalCalls(0);
                statusMap.put(status, info);
            }

            callsByType.put(type, statusMap);
        }

        var totalCallsInTraining = callRepository.findAllByTrainingId(training.getId());
        totalCallsInTraining.forEach(call -> {

            List<CallLog> logs = Optional.ofNullable(call.getCallLogs())
                    .orElse(List.of());

            for (CallType type : CallType.values()) {

                CallLog lastLogByType = logs.stream()
                        .filter(Objects::nonNull)
                        .filter(log -> log.getType() == type)
                        .max(Comparator.comparing(CallLog::getDate))
                        .orElse(null);

                CallStatus status;

                if (lastLogByType == null) {
                    status = CallStatus.NO_CALLED;
                } else {
                    status = lastLogByType.getStatus();
                }

                CallsInfo info = callsByType
                        .get(type)
                        .get(status);

                info.setTotalCalls(info.getTotalCalls() + 1);
            }
        });
        List<CallTypeStats> finalList = new ArrayList<>();

        for (CallType type : callsByType.keySet()) {

            Collection<CallsInfo> infos = callsByType.get(type).values();

            int totalCalls = totalCallsInTraining.size();

            int done = callsByType.get(type)
                    .get(CallStatus.DONE)
                    .getTotalCalls();

            int anotherCampus = callsByType.get(type)
                    .get(CallStatus.ANOTHER_CAMPUS)
                    .getTotalCalls();

            int notInterested = callsByType.get(type)
                    .get(CallStatus.NOT_INTERESTED)
                    .getTotalCalls();

            int nextDate = callsByType.get(type)
                    .get(CallStatus.NEXT_DATE)
                    .getTotalCalls();

            int forConfirmation = callsByType.get(type)
                    .get(CallStatus.FOR_CONFIRMATION)
                    .getTotalCalls();

            int notAnswered = callsByType.get(type)
                    .get(CallStatus.NOT_ANSWERED)
                    .getTotalCalls();

            int noCalled = callsByType.get(type)
                    .get(CallStatus.NO_CALLED)
                    .getTotalCalls();

            int reAgendado = callsByType.get(type)
                    .get(CallStatus.RE_SCHEDULED)
                    .getTotalCalls();

            CallTypeStats stats = new CallTypeStats();
            stats.setCallType(type);
            stats.setStatuses(new ArrayList<>(infos));

            int cuadre = totalCalls - (
                    done + anotherCampus + notInterested + nextDate + forConfirmation + notAnswered + noCalled + reAgendado
            );

            stats.setCuadre(cuadre);

            double effectiveness = totalCalls > 0
                    ? Math.round(((double) (done + anotherCampus) / totalCalls) * 100.0)
                    : 0.0;

            stats.setEffectivenessPercentage(effectiveness);

            double projectedCalls = totalCalls > 0
                    ? Math.round(((double) (totalCalls - notInterested - nextDate) / totalCalls) * 100.0)
                    : 0.0;

            stats.setProjectedCallsPercentage(projectedCalls);

            finalList.add(stats);
        }

        return finalList;
    }
}
