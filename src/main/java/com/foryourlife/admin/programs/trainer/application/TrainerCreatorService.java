package com.foryourlife.admin.programs.trainer.application;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import org.springframework.stereotype.Service;

@Service
public class TrainerCreatorService {
    private TrainerRepository repository;

    public TrainerCreatorService(TrainerRepository repository) {
        this.repository = repository;
    }

    public void createTrainer(Trainer trainer){
        repository.saveTrainer(trainer);
    }
}
