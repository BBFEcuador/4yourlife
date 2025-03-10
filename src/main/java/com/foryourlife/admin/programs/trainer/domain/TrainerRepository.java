package com.foryourlife.admin.programs.trainer.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerRepository {
    void saveTrainer(Trainer trainer);

    Optional<Trainer> findTrainerById(String id);

    List<Trainer> getTrainers();
    List<Trainer> getAvailableTrainers(LocalDate startDate,LocalDate endDate);


}
