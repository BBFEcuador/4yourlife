package com.foryourlife.admin.programs.trainer.application;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerQueryService {

    private final TrainingRepository trainingRepository;
    private TrainerRepository repository;


    public TrainerQueryService(TrainerRepository repository, TrainingRepository trainingRepository) {
        this.repository = repository;
        this.trainingRepository = trainingRepository;
    }

    public List<Trainer> findTrainers(){
        return repository.getTrainers();
    }
    public Page<Trainer> findTrainers(Pageable pageable, Criteria criteria){
        return repository.getTrainers(pageable,criteria);
    }
    public List<Trainer> findTrainersAvailable(String trainingId){
        var training = trainingRepository.findById(trainingId).orElseThrow();
        return repository.getAvailableTrainers(training.getNextLevel().getStartDate(), training.getNextLevel().getEndDate());
    }

    public Optional<Trainer> findTrainerById(String trainerId) {
        return repository.findTrainerById(trainerId);
    }
}
