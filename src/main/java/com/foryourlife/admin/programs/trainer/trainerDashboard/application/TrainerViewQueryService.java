package com.foryourlife.admin.programs.trainer.trainerDashboard.application;

import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.TrainerFocusView;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.focus.TrainerFocusViewRepository;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.life.TrainerLifeView;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.life.TrainerViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerViewQueryService {
    private final TrainerViewRepository trainerRepository;
    private final TrainerFocusViewRepository trainerFocusViewRepository;

    public TrainerViewQueryService(TrainerViewRepository trainerRepository, TrainerFocusViewRepository trainerFocusViewRepository) {
        this.trainerRepository = trainerRepository;
        this.trainerFocusViewRepository = trainerFocusViewRepository;
    }

    public List<TrainerLifeView> getTrainerView(String id) {
        return trainerRepository.getTrainerLifeViewByTraining(id);
    }

    public TrainerFocusView getFocusView(String trainingId) {
        return trainerFocusViewRepository.getTrainerFocusViewByTrainingId(trainingId);
    }
}
