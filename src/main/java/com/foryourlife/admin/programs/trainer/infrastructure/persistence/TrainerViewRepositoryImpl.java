package com.foryourlife.admin.programs.trainer.infrastructure.persistence;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.trainer.domain.*;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrainerViewRepositoryImpl implements TrainerViewRepository {
    private final TrainingRepository jpaTrainingRepository;
    private final AttendanceRepository attendanceRepository;
    private final PromiseRepository promiseRepository;

    public TrainerViewRepositoryImpl(TrainingRepository jpaTrainingRepository, AttendanceRepository attendanceRepository, PromiseRepository promiseRepository) {
        this.jpaTrainingRepository = jpaTrainingRepository;
        this.attendanceRepository = attendanceRepository;
        this.promiseRepository = promiseRepository;
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

        Map<String, Promise> promiseMap = promises.stream()
                .filter(p -> p.getUser() != null)
                .collect(Collectors.toMap(p -> p.getUser().getId(), p -> p, (a, b) -> a));


        List<UserDashboardDto> userDashboards = attendances.stream()
                .filter(a -> a.getUser() != null)
                .map(a -> {
                    Promise userPromise = promiseMap.get(a.getUser().getId());
                    return new UserDashboardDto(
                            a.getUser().getName(),
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

        AttendanceDashboard attendanceDashboard = buildAttendanceDashboard(attendances);
        PromiseDashboard promiseDashboard = buildPromiseDashboard(promises);

        return new TrainerLifeView(
                training.getName(),
                training.getOriginalTeam() != null && training.getOriginalTeam().getTrainer() != null
                        ? training.getOriginalTeam().getTrainer().getName()
                        : "Sin entrenador",
                attendanceDashboard,
                promiseDashboard,
                userDashboards
        );
    }

    private AttendanceDashboard buildAttendanceDashboard(List<Attendance> attendances) {
        if (attendances == null || attendances.isEmpty()) {
            return new AttendanceDashboard(0, 0, 0, 0);
        }

        long fridayCount = attendances.stream()
                .filter(a -> a.getFridayAttendance() == AttendanceStatus.ASISTIO)
                .count();
        long saturdayCount = attendances.stream()
                .filter(a -> a.getSaturdayAttendance() == AttendanceStatus.ASISTIO)
                .count();
        long sundayCount = attendances.stream()
                .filter(a -> a.getSundayAttendance() == AttendanceStatus.ASISTIO)
                .count();

        return new AttendanceDashboard(fridayCount, saturdayCount, sundayCount, attendances.size());
    }

    private PromiseDashboard buildPromiseDashboard(List<Promise> promises) {
        if (promises == null || promises.isEmpty()) {
            return new PromiseDashboard(0, 0, 0, 0, 0);
        }

        int totalFirst = promises.stream().mapToInt(Promise::getFirstPromise).sum();
        int totalSecond = promises.stream().mapToInt(Promise::getSecondPromise).sum();
        int totalThird = promises.stream().mapToInt(Promise::getThirdPromise).sum();
        int totalAchieved = promises.stream().mapToInt(Promise::getAchievedCount).sum();
        int totalPaid = promises.stream().mapToInt(Promise::getPaidCount).sum();

        return new PromiseDashboard(totalFirst, totalSecond, totalThird, totalAchieved, totalPaid);
    }
}
