package com.foryourlife.admin.dashboard.trainerDashboard.infrastructure.utils;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.life.*;
import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class TrainerLifeViewRepositoryImpl implements TrainerViewRepository {
    private final TrainingRepository jpaTrainingRepository;
    private final AttendanceRepository attendanceRepository;
    private final TrainingDashboardUtils trainingDashboardUtils;
    private final PromiseRepository promiseRepository;
    private final TeamRepository teamRepository;

    public TrainerLifeViewRepositoryImpl(TrainingRepository jpaTrainingRepository, AttendanceRepository attendanceRepository, TrainingDashboardUtils trainingDashboardUtils, PromiseRepository promiseRepository, TeamRepository teamRepository) {
        this.jpaTrainingRepository = jpaTrainingRepository;
        this.attendanceRepository = attendanceRepository;
        this.trainingDashboardUtils = trainingDashboardUtils;
        this.promiseRepository = promiseRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<TrainerLifeView> getTrainerLifeViewByTraining(String trainingId) {
        var training = jpaTrainingRepository.findById(trainingId)
                .orElseThrow(() -> new BaseException("Entrenamiento no encontrado", List.of()));

        Training life1 = null, life2 = null, life3 = null;

        if (training.getCourseLevel().equals(CourseLevel.LIFE)) {
            life1 = training;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_2)) {
            life2 = training;
            life1 = jpaTrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_3)) {
            life3 = training;
            life2 = jpaTrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            life1 = (life2 != null) ? jpaTrainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
        }

        List<TrainerLifeView> dashboards = new ArrayList<>();
        if (life1 != null) dashboards.add(buildTrainingDashboard(life1));
        if (life2 != null) dashboards.add(buildTrainingDashboard(life2));
        if (life3 != null) dashboards.add(buildTrainingDashboard(life3));

        return dashboards;
    }

    private TrainerLifeView buildTrainingDashboard(Training training) {
        if (training == null) return null;

        List<Attendance> attendances = attendanceRepository.findAttendanceByTraining(training.getId());
        List<Promise> promises = promiseRepository.findByTrainingId(training.getId());
        Map<String, Participant> participants = trainingDashboardUtils.loadParticipants(attendances);

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

        LifeAttendanceDashboard lifeAttendanceDashboard = buildAttendanceDashboard(attendances);
        PromiseDashboard promiseDashboard = buildPromiseDashboard(promises);

        int finalParticipants = Math.toIntExact(lifeAttendanceDashboard.getTotalParticipants() + lifeAttendanceDashboard.getTotalParticipants());
        double enrollmentIndex = (finalParticipants) / (double) promiseDashboard.getTotalAchieved();
        double realEnrollmentIndex = attendances.size() / (double) promiseDashboard.getTotalAchieved();
        int enrollerCount = promises.stream().filter(p -> p.getAchievedCount() > 0).map(p -> p.getUser().getId()).collect(Collectors.toSet()).size();
        double enrollerPercentage = (double) enrollerCount / finalParticipants;

        promiseDashboard.setEnrollmentIndex(enrollmentIndex);
        promiseDashboard.setRealEnrollmentIndex(realEnrollmentIndex);
        promiseDashboard.setEnrollerPersonsCount(enrollerCount);
        promiseDashboard.setEnrollerPersonsPercent(enrollerPercentage);

        var trainerName = training.getOriginalTeam().getTrainer().getName() != null ? training.getOriginalTeam().getTrainer().getName() : teamRepository.findByTrainingId(training.getId()).map(t -> t.getTrainer().getName()).orElse("Sin entrenador");

        return new TrainerLifeView(
                training.getName(),
                trainerName,
                training.getStartDate().toString() + " - " + training.getEndDate().toString(),
                lifeAttendanceDashboard,
                promiseDashboard,
                userDashboards,
                trainingDashboardUtils.buildLingererStats(training, attendances, participants)
        );
    }

    private LifeAttendanceDashboard buildAttendanceDashboard(List<Attendance> attendances) {
        if (attendances == null || attendances.isEmpty()) {
            return new LifeAttendanceDashboard(0, 0, 0, 0, 0, 0, 0, 0, 0, 0.0);
        }

        Predicate<Attendance> isParticipant = a ->
                a.getUser() != null &&
                        a.getUser().getEntityMap() != null &&
                        a.getUser().getEntityMap().stream().anyMatch(e ->
                                e.getEntity().equals(UserType.PARTICIPANT.name())
                        ) &&
                        a.getUser().getEntityMap().stream().noneMatch(e ->
                                e.getEntity().equals(UserType.MASTER_LIFE.name())
                        );

        Predicate<Attendance> isMasterLife = a ->
                a.getUser() != null &&
                        a.getUser().getEntityMap() != null &&
                        a.getUser().getEntityMap().stream()
                                .anyMatch(e -> e.getEntity().equals(UserType.MASTER_LIFE.name()));

        long fridayCount = attendances.stream().filter(isParticipant).filter(a -> a.getFridayAttendance() == AttendanceStatus.ASISTIO).count();
        long saturdayCount = attendances.stream().filter(isParticipant).filter(a -> a.getSaturdayAttendance() == AttendanceStatus.ASISTIO).count();
        long sundayCount = attendances.stream().filter(isParticipant).filter(a -> a.getSundayAttendance() == AttendanceStatus.ASISTIO).count();

        long masterFridayCount = attendances.stream().filter(isMasterLife).filter(a -> a.getFridayAttendance() == AttendanceStatus.ASISTIO).count();
        long masterSaturdayCount = attendances.stream().filter(isMasterLife).filter(a -> a.getSaturdayAttendance() == AttendanceStatus.ASISTIO).count();
        long masterSundayCount = attendances.stream().filter(isMasterLife).filter(a -> a.getSundayAttendance() == AttendanceStatus.ASISTIO).count();

        long deserterCount = attendances.stream().filter(isParticipant)
                .filter(a -> a.getFridayAttendance() == AttendanceStatus.NO_ASISTIO ||
                        a.getFridayAttendance() == AttendanceStatus.DESERTO ||
                        a.getSaturdayAttendance() == AttendanceStatus.NO_ASISTIO ||
                        a.getSaturdayAttendance() == AttendanceStatus.DESERTO ||
                        a.getSundayAttendance() == AttendanceStatus.NO_ASISTIO ||
                        a.getSundayAttendance() == AttendanceStatus.DESERTO
                )
                .count();

        long totalParticipants = fridayCount + saturdayCount + sundayCount;
        long totalMasterParticipants = masterFridayCount + masterSaturdayCount + masterSundayCount;

        double deserterPercentage = (double) deserterCount / totalParticipants;

        return new LifeAttendanceDashboard(
                fridayCount,
                saturdayCount,
                sundayCount,
                totalParticipants,
                masterFridayCount,
                masterSaturdayCount,
                masterSundayCount,
                totalMasterParticipants,
                deserterCount,
                deserterPercentage
        );
    }

    private PromiseDashboard buildPromiseDashboard(List<Promise> promises) {
        if (promises == null || promises.isEmpty()) {
            return new PromiseDashboard(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        Predicate<Promise> isParticipant = p ->
                p.getUser() != null &&
                        p.getUser().getEntityMap() != null &&
                        p.getUser().getEntityMap().stream()
                                .anyMatch(e -> e.getEntity().equals(UserType.PARTICIPANT.name()));

        Predicate<Promise> isMasterLife = p ->
                p.getUser() != null &&
                        p.getUser().getEntityMap() != null &&
                        p.getUser().getEntityMap().stream()
                                .anyMatch(e -> e.getEntity().equals(UserType.MASTER_LIFE.name()));

        int totalFirst = promises.stream().filter(isParticipant).mapToInt(Promise::getFirstPromise).sum();
        int totalSecond = promises.stream().filter(isParticipant).mapToInt(Promise::getSecondPromise).sum();
        int totalThird = promises.stream().filter(isParticipant).mapToInt(Promise::getThirdPromise).sum();
        int totalAchieved = promises.stream().filter(isParticipant).mapToInt(Promise::getAchievedCount).sum();
        int totalPaid = promises.stream().filter(isParticipant).mapToInt(Promise::getPaidCount).sum();

        int totalFirstMaster = promises.stream().filter(isMasterLife).mapToInt(Promise::getFirstPromise).sum();
        int totalSecondMaster = promises.stream().filter(isMasterLife).mapToInt(Promise::getSecondPromise).sum();
        int totalThirdMaster = promises.stream().filter(isMasterLife).mapToInt(Promise::getThirdPromise).sum();
        int totalAchievedMaster = promises.stream().filter(isMasterLife).mapToInt(Promise::getAchievedCount).sum();
        int totalPaidMaster = promises.stream().filter(isMasterLife).mapToInt(Promise::getPaidCount).sum();

        return new PromiseDashboard(
                totalFirst,
                totalSecond,
                totalThird,
                totalFirstMaster,
                totalSecondMaster,
                totalThirdMaster,
                totalThirdMaster,
                totalAchievedMaster,
                totalPaidMaster,
                totalAchieved,
                totalPaid
        );
    }
}
