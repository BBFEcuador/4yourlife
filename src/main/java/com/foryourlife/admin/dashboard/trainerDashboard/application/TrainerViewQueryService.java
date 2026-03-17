package com.foryourlife.admin.dashboard.trainerDashboard.application;

import com.foryourlife.admin.dashboard.trainerDashboard.domain.focus.TrainerFocusView;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.focus.TrainerFocusViewRepository;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.life.TrainerLifeView;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.life.TrainerViewRepository;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.your.TrainerYourView;
import com.foryourlife.admin.dashboard.trainerDashboard.domain.your.TrainerYourViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerViewQueryService {
    private final TrainerViewRepository trainerRepository;
    private final TrainerFocusViewRepository trainerFocusViewRepository;
    private final TrainerYourViewRepository trainerYourViewRepository;

    public TrainerViewQueryService(TrainerViewRepository trainerRepository, TrainerFocusViewRepository trainerFocusViewRepository, TrainerYourViewRepository trainerYourViewRepository) {
        this.trainerRepository = trainerRepository;
        this.trainerFocusViewRepository = trainerFocusViewRepository;
        this.trainerYourViewRepository = trainerYourViewRepository;
    }

    public List<TrainerLifeView> getTrainerView(String id) {
        return trainerRepository.getTrainerLifeViewByTraining(id);
    }

    public TrainerFocusView getFocusView(String trainingId) {
        return trainerFocusViewRepository.getTrainerFocusViewByTrainingId(trainingId);
    }

    public TrainerYourView getYourView(String trainingId) {
        return trainerYourViewRepository.getTrainerYourViewByTrainingId(trainingId);
    }
}
