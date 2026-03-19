package com.foryourlife.admin.dashboard.operativeAssitantDashboard.infrastructure.utils;

import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.common.WeekendReport;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life.OperativeLifeDashboard;
import com.foryourlife.admin.dashboard.operativeAssitantDashboard.domain.life.OperativeLifeDashboardRepository;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.life.TrainerLifeView;
import com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils.TrainingDashboardUtils;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.programs.training.infrastructure.JPATrainingRepository;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImplOperativeLifeDashboardRepository implements OperativeLifeDashboardRepository {
    private final TrainingRepository trainingRepository;
    private final ImplOperativeAssistantDashboardRepository implOperativeAssistantDashboardRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final AttendanceRepository attendanceRepository;
    private final PromiseRepository promiseRepository;
    private final JPATrainingRepository jPATrainingRepository;

    public ImplOperativeLifeDashboardRepository(TrainingRepository trainingRepository, ImplOperativeAssistantDashboardRepository implOperativeAssistantDashboardRepository, TrainingDashboardUtils trainingDashboardUtils, AttendanceRepository attendanceRepository, PromiseRepository promiseRepository, JPATrainingRepository jPATrainingRepository) {
        this.trainingRepository = trainingRepository;
        this.implOperativeAssistantDashboardRepository = implOperativeAssistantDashboardRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.attendanceRepository = attendanceRepository;
        this.promiseRepository = promiseRepository;
        this.jPATrainingRepository = jPATrainingRepository;
    }

    @Override
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
        var callInfoList = implOperativeAssistantDashboardRepository.buildCallsTable(training);

        var team = training.getOriginalTeam();

        var trainerName = trainingDashboardUtils.getTrainerName(training, team);
        var weekendReport = BuildWeekendReport(training, team);

        return new OperativeLifeDashboard(
                training.getName(),
                trainerName,
                weekendReport,
                callInfoList
        );
    }

    public WeekendReport BuildWeekendReport(Training training, Team team) {
        var attendances = attendanceRepository.findAttendanceByTraining(training.getId());
        var teamT = training.getOriginalTeam() != null ? training.getOriginalTeam() : team;
        int participantDeclarations = 0, masterLifesDeclarations = 0, enrolmentsDeclarations;
        if (training.getCourseLevel() != CourseLevel.YOUR) {
            var declarations = promiseRepository.findByTrainingId(training.getId());

            if (!declarations.isEmpty()) {
                for (var declaration : declarations) {
                    var participant = teamT.getUsers().stream().filter(user -> user.getUser().getId().equals(declaration.getUser().getId())).findFirst();
                    if (participant.isPresent()) {
                        participantDeclarations += declaration.getThirdPromise();
                    } else {
                        var masterLife = teamT.getMasterLife().stream().filter(ml -> ml.getUser().getId().equals(declaration.getUser().getId())).findFirst();
                        if (masterLife.isPresent()) {
                            masterLifesDeclarations += declaration.getThirdPromise();
                        }
                    }
                }
            }
        }

        var participantAttendances = attendances.stream()
                .filter(attendance -> teamT.getUsers().stream()
                        .anyMatch(participant -> participant.getUser().getId().equals(attendance.getUser().getId()))
                ).count();
        var masterLifeAttendances = attendances.stream()
                .filter(attendance -> teamT.getMasterLife().stream()
                        .anyMatch(ml -> ml.getUser().getId().equals(attendance.getUser().getId()))
                ).count();

        int totalDeclarations = participantDeclarations + masterLifesDeclarations;

        double declarationIndex = (double) (participantAttendances + masterLifeAttendances) / totalDeclarations * 100;

        return new WeekendReport(
                teamT.getUsers().size(),
                (int) participantAttendances,
                participantDeclarations,
                teamT.getMasterLife().size(),
                (int) masterLifeAttendances,
                masterLifesDeclarations,
                teamT.getUsers().size() + teamT.getMasterLife().size(),
                (int) (participantAttendances + masterLifeAttendances),
                totalDeclarations,
                0,// TODO: 3/19/2026 encontrar por query totalEnrollmentsCount)
                declarationIndex,
                0 // TODO: 3/19/2026 calcular realIndex
        );
    }
}
