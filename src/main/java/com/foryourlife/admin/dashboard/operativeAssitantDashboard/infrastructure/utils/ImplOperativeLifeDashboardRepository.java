package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.admin.crm.callLogs.domain.CallLog;
import com.foryourlife.admin.crm.callLogs.domain.CallStatus;
import com.foryourlife.admin.crm.callLogs.domain.CallType;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life.*;
import com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils.TrainingDashboardUtils;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.programs.training.infrastructure.JPATrainingRepository;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
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
    private final InvitationRepository invitationRepository;

    public ImplOperativeLifeDashboardRepository(TrainingRepository trainingRepository, TrainingDashboardUtils trainingDashboardUtils, AttendanceRepository attendanceRepository, PromiseRepository promiseRepository, JPATrainingRepository jPATrainingRepository, CallRepository callRepository, TeamRepository teamRepository, InvitationRepository invitationRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.attendanceRepository = attendanceRepository;
        this.promiseRepository = promiseRepository;
        this.jPATrainingRepository = jPATrainingRepository;
        this.callRepository = callRepository;
        this.teamRepository = teamRepository;
        this.invitationRepository = invitationRepository;
    }

    @Override
    @Transactional
    public List<OperativeLifeDashboard> getOperativeLifeDashboard(String trainingId) {
        var training = trainingRepository.findById(trainingId).orElseThrow(() -> new RuntimeException("Training not found"));

        Training life1 = null, life2 = null, life3 = null;

        if (training.getCourseLevel().equals(CourseLevel.LIFE)) {
            life1 = training;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_2)) {
            life2 = training;
            life1 = jPATrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_3)) {
            life3 = training;
            life2 = jPATrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            life1 = (life2 != null) ? jPATrainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
        }

        List<OperativeLifeDashboard> dashboards = new ArrayList<>();
        if (life1 != null) dashboards.add(buildOperativeLifeDashboard(life1));
        if (life2 != null) dashboards.add(buildOperativeLifeDashboard(life2));
        if (life3 != null) dashboards.add(buildOperativeLifeDashboard(life3));
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
        int participantDeclarations = 0, masterLifesDeclarations = 0, enrolmentsDeclarations;
        if (training.getCourseLevel() != CourseLevel.YOUR) {
            var declarations = promiseRepository.findByTrainingId(training.getId());

            if (!declarations.isEmpty()) {
                for (var declaration : declarations) {
                    var participant = team.getUsers().stream().filter(user -> user.getUser().getId().equals(declaration.getUser().getId())).findFirst();
                    if (participant.isPresent()) {
                        participantDeclarations += declaration.getThirdPromise();
                    } else {
                        var masterLife = team.getMasterLife().stream().filter(ml -> ml.getUser().getId().equals(declaration.getUser().getId())).findFirst();
                        if (masterLife.isPresent()) {
                            masterLifesDeclarations += declaration.getThirdPromise();
                        }
                    }
                }
            }
        }

        var participantAttendances = attendances.stream()
                .filter(attendance -> team.getUsers().stream()
                        .anyMatch(participant -> participant.getUser().getId().equals(attendance.getUser().getId()))
                ).count();
        var masterLifeAttendances = attendances.stream()
                .filter(attendance -> team.getMasterLife().stream()
                        .anyMatch(ml -> ml.getUser().getId().equals(attendance.getUser().getId()))
                ).count();

        int totalDeclarations = participantDeclarations + masterLifesDeclarations;

        double declarationIndex = totalDeclarations > 0
                ? ((double) (participantAttendances + masterLifeAttendances) / totalDeclarations) * 100
                : 0.0;

        var participants = trainingDashboardUtils.loadParticipants(attendances);

        List<String> invitationTokens = participants.values().stream()
                .map(Participant::getInvitationToken)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<String, Invitation> invitationsByToken = invitationTokens.isEmpty()
                ? Map.of()
                : invitationRepository.findAllByTokenIn(invitationTokens)
                  .stream()
                  .collect(Collectors.toMap(
                          Invitation::getToken,
                          i -> i
                  ));

        int trainingInvitations = (int) invitationsByToken.values().stream().filter(invitation -> invitation.getEnrolled().getTrainingName() != null && invitation.getEnrolled().getTrainingName().equals(training.getName())).count();

        double realIndex = trainingInvitations > 0
                ? ((double) (participantAttendances + masterLifeAttendances) / totalDeclarations) * 100
                : 0.0;

        return new WeekendLifeReport(
                team.getUsers().size(),
                (int) participantAttendances,
                participantDeclarations,
                team.getMasterLife().size(),
                (int) masterLifeAttendances,
                masterLifesDeclarations,
                team.getUsers().size() + team.getMasterLife().size(),
                (int) (participantAttendances + masterLifeAttendances),
                totalDeclarations,
                trainingInvitations,
                declarationIndex,
                realIndex
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

        callRepository.findAllByTrainingId(training.getId())
                .forEach(call -> {

                    if (call.getCallLogs() == null || call.getCallLogs().isEmpty()) {
                        CallsInfo info = callsByType
                                .get(CallType.LOGISTIC)
                                .get(CallStatus.NO_CALLED);

                        info.setTotalCalls(info.getTotalCalls() + 1);
                        return;
                    }

                    CallLog lastCallLog = call.getCallLogs().stream()
                            .filter(Objects::nonNull)
                            .max(Comparator.comparing(CallLog::getDate))
                            .orElse(null);

                    if (lastCallLog == null) {
                        CallsInfo info = callsByType
                                .get(CallType.LOGISTIC)
                                .get(CallStatus.NO_CALLED);

                        info.setTotalCalls(info.getTotalCalls() + 1);
                        return;
                    }

                    CallType type = lastCallLog.getType();
                    CallStatus status = lastCallLog.getStatus();

                    CallsInfo info = callsByType
                            .get(type)
                            .get(status);

                    info.setTotalCalls(info.getTotalCalls() + 1);
                });

        List<CallTypeStats> finalList = new ArrayList<>();

        for (CallType type : callsByType.keySet()) {

            Collection<CallsInfo> infos = callsByType.get(type).values();

            int totalCalls = infos.stream()
                    .mapToInt(CallsInfo::getTotalCalls)
                    .sum();

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

            CallTypeStats stats = new CallTypeStats();
            stats.setCallType(type);
            stats.setStatuses(new ArrayList<>(infos));

            stats.setCuadre(stats.getStatuses().size() - totalCalls);

            double effectiveness = ((double) (done + anotherCampus) / totalCalls) ;

            stats.setEffectivenessPercentage(effectiveness);

            double projectedCalls = totalCalls == 0
                    ? 0
                    : ((double) (totalCalls - notInterested - nextDate) / totalCalls);

            stats.setProjectedCallsPercentage(projectedCalls);

            finalList.add(stats);
        }

        return finalList;
    }
}
