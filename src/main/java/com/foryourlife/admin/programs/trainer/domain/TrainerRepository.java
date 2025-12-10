package com.foryourlife.admin.programs.trainer.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerRepository {
    void saveTrainer(Trainer trainer);

    Optional<Trainer> findTrainerById(String id);

    List<Trainer> getTrainers();
    Page<Trainer> getTrainers(Pageable pageable, Criteria criteria);
    List<Trainer> getAvailableTrainers(LocalDate startDate,LocalDate endDate);
    LoginTrainerResponse loginTrainer(String email, String password);
    Long countTrainers();
}
