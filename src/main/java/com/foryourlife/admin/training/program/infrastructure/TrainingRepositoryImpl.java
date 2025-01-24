package com.foryourlife.admin.training.program.infrastructure;

import com.foryourlife.admin.training.program.domain.Training;
import com.foryourlife.admin.training.program.domain.TrainingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingRepositoryImpl implements TrainingRepository {

    private final JPATrainingRepository repository;

    public TrainingRepositoryImpl(JPATrainingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<Training> trainings) {
        this.repository.saveAll(trainings);
    }

    @Override
    public void save(Training trainings) {
        this.repository.save(trainings);
    }

    @Override
    public List<Training> getAll() {
        return this.repository.findAll();
    }
}
