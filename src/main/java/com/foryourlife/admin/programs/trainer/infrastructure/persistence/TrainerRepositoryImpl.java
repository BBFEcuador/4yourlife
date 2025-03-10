package com.foryourlife.admin.programs.trainer.infrastructure.persistence;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerRepositoryImpl implements TrainerRepository {

    private JPATrainerRepository repository;

    public TrainerRepositoryImpl(JPATrainerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveTrainer(Trainer trainer) {
        this.repository.save(trainer);
    }

    @Override
    public Optional<Trainer> findTrainerById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public List<Trainer> getTrainers() {
        return this.repository.findAll();
    }

    @Override
    public List<Trainer> getAvailableTrainers(LocalDate startDate, LocalDate endDate) {
        return this.repository.findAvailableTrainers(startDate,endDate);
    }
}
