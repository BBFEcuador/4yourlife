package com.foryourlife.admin.programs.trainer.application;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerCreatorService {
    private TrainerRepository repository;

    public TrainerCreatorService(TrainerRepository repository) {
        this.repository = repository;
    }

    public void createTrainer(Trainer trainer){
        repository.saveTrainer(trainer);
    }

    public void updateTrainer(Trainer trainer){
        repository.findTrainerById(trainer.getId()).orElseThrow(() -> new BaseException("Not found", List.of("Trainer not exist")));
        repository.saveTrainer(trainer);
    }
}
