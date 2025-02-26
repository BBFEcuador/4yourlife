package com.foryourlife.admin.programs.trainer.domain;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository {
    void saveTrainer(Trainer trainer);

    Optional<Trainer> findTrainerById(String id);

    List<Trainer> getTrainers();


}
