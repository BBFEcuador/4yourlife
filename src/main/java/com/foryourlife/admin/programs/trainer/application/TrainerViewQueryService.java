package com.foryourlife.admin.programs.trainer.application;

import com.foryourlife.admin.programs.trainer.domain.TrainerLifeView;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import com.foryourlife.admin.programs.trainer.domain.TrainerViewRepository;
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
