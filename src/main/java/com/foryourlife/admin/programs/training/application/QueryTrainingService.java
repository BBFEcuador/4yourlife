package com.foryourlife.admin.programs.training.application;

import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryTrainingService {
    private final TrainingRepository repository;

    public QueryTrainingService(TrainingRepository repository) {
        this.repository = repository;
    }

    public Training getTrainingById(String id) {
        return this.repository.findById(id).orElseThrow(
                () -> new BaseException("Training not found", List.of("The training does not exist"))
        );
    }

    public Page<Training> getAllTrainings(Pageable pageable, Criteria criteria) {
        return this.repository.getAll(pageable,criteria);
    }

    public List<Training> match(Criteria criteria) {
        return this.repository.match(criteria);
    }
}
