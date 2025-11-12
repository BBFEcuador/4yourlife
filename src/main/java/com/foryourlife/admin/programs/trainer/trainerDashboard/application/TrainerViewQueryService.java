package com.foryourlife.admin.programs.trainer.trainerDashboard.application;

import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.life.TrainerLifeView;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.life.TrainerViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerViewQueryService {
    private TrainerViewRepository trainerRepository;

    public TrainerViewQueryService(TrainerViewRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public List<TrainerLifeView> getTrainerView(String id) {
        return trainerRepository.getTrainerLifeViewByTraining(id);
    }
}
