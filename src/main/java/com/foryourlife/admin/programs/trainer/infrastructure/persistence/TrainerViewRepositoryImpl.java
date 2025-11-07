package com.foryourlife.admin.programs.trainer.infrastructure.persistence;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.trainer.domain.AttendanceDashboard;
import com.foryourlife.admin.programs.trainer.domain.TrainerLifeView;
import com.foryourlife.admin.programs.trainer.domain.TrainerViewRepository;
import com.foryourlife.admin.programs.trainer.domain.TrainingDashboardDto;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerViewRepositoryImpl implements TrainerViewRepository {
    private final TrainingRepository jpaTrainingRepository;
    private final AttendanceRepository attendanceRepository;

    public TrainerViewRepositoryImpl(TrainingRepository jpaTrainingRepository, AttendanceRepository attendanceRepository) {
        this.jpaTrainingRepository = jpaTrainingRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public TrainerLifeView getTrainerLifeViewByTraining(String trainingId) {
        var training = jpaTrainingRepository.findById(trainingId)
                .orElseThrow(() -> new BaseException("Entrenamiento no encontrado", List.of()));

        Training life1 = null;
        Training life2 = null;
        Training life3 = null;
        List<Attendance> attendancesLife1 = null;
        List<Attendance> attendancesLife2 = null;
        List<Attendance> attendancesLife3 = null;

        if (training.getCourseLevel().equals(CourseLevel.LIFE)) {
            life1 = training;
            attendancesLife1 = attendanceRepository.findAttendanceByTraining(life1.getId());
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_2)) {
            life2 = training;
            life1 = jpaTrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            attendancesLife2 = attendanceRepository.findAttendanceByTraining(life2.getId());
            attendancesLife1 = attendanceRepository.findAttendanceByTraining(life1.getId());
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_3)) {
            life3 = training;
            life2 = jpaTrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            life1 = (life2 != null) ? jpaTrainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
            attendancesLife3 = attendanceRepository.findAttendanceByTraining(life3.getId());
            attendancesLife2 = attendanceRepository.findAttendanceByTraining(life2.getId());
            attendancesLife1 = attendanceRepository.findAttendanceByTraining(life1.getId());
        }

        return new TrainerLifeView(
                List.of(
                        new TrainingDashboardDto(
                                life1 != null ? life1.getName() : null,
                                life1 != null ? life1.getOriginalTeam().getTrainer().getName() : null,
                                (AttendanceDashboard) List.of(
                                        new AttendanceDashboard(
//                                                attendancesLife1
                                        )
                                )
                        ),
                        new TrainingDashboardDto(
                                life2 != null ? life2.getName() : null,
                                life2 != null ? life2.getOriginalTeam().getTrainer().getName() : null,
                                (AttendanceDashboard) List.of(

                                )
                        ),
                        new TrainingDashboardDto(
                                life3 != null ? life3.getName() : null,
                                life3 != null ? life3.getOriginalTeam().getTrainer().getName() : null,
                                (AttendanceDashboard) List.of(
                                )
                        )
                )
        );
    }

}
