package com.foryourlife.admin.programs.trainer.infrastructure.persistence;

import com.foryourlife.admin.programs.trainer.domain.TrainerLifeView;
import com.foryourlife.admin.programs.trainer.domain.TrainerViewRepository;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerViewRepositoryImpl implements TrainerViewRepository {
    private final TrainingRepository jpaTrainingRepository;

    public TrainerViewRepositoryImpl(TrainingRepository jpaTrainingRepository) {
        this.jpaTrainingRepository = jpaTrainingRepository;
    }

    @Override
    public TrainerLifeView getTrainerLifeViewByTraining(String trainingId) {
        var training = jpaTrainingRepository.findById(trainingId)
                .orElseThrow(() -> new BaseException("Entrenamiento no encontrado", List.of()));

        Training life1 = null;
        Training life2 = null;
        Training life3 = null;
        Training graduate = null;

        if (training.getCourseLevel().equals(CourseLevel.LIFE)) {
            life1 = training;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_2)) {
            life2 = training;
            life1 = jpaTrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_3)) {
            life3 = training;
            life2 = jpaTrainingRepository.findByNextLevel_Id(training.getId()).orElse(null);
            life1 = (life2 != null) ? jpaTrainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
        } else if (training.getCourseLevel().equals(CourseLevel.LIFE_GRADUATE)) {
            graduate = training;
            life3 = jpaTrainingRepository.findByNextLevel_Id(graduate.getId()).orElse(null);
            life2 = (life3 != null) ? jpaTrainingRepository.findByNextLevel_Id(life3.getId()).orElse(null) : null;
            life1 = (life2 != null) ? jpaTrainingRepository.findByNextLevel_Id(life2.getId()).orElse(null) : null;
        }

        return new TrainerLifeView(life1, life2, life3, graduate);
    }

}
