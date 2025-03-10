package com.foryourlife.admin.programs.trainer.application;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerFinderService {

    private TrainerRepository repository;

    public TrainerFinderService(TrainerRepository repository) {
        this.repository = repository;
    }

    public List<Trainer> findTrainers(){
        return repository.getTrainers();
    }
    public List<Trainer> findTrainersAvailable(LocalDate startDate, LocalDate endDate){
        return repository.getAvailableTrainers(startDate,endDate);
    }

    public Optional<Trainer> findTrainerById(String trainerId) {
        return repository.findTrainerById(trainerId);
    }
}
