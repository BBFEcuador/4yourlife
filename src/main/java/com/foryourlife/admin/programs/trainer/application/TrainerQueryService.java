package com.foryourlife.admin.programs.trainer.application;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerQueryService {

    private TrainerRepository repository;

    public TrainerQueryService(TrainerRepository repository) {
        this.repository = repository;
    }

    public List<Trainer> findTrainers(){
        return repository.getTrainers();
    }
    public Page<Trainer> findTrainers(Pageable pageable, Criteria criteria){
        return repository.getTrainers(pageable,criteria);
    }
    public List<Trainer> findTrainersAvailable(LocalDate startDate, LocalDate endDate){
        return repository.getAvailableTrainers(startDate,endDate);
    }

    public Optional<Trainer> findTrainerById(String trainerId) {
        return repository.findTrainerById(trainerId);
    }
}
