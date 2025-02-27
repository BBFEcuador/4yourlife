package com.foryourlife.admin.programs.training.domain;

import com.foryourlife.shared.domain.criteria.Criteria;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingRepository {
    void saveAll(List<Training> trainings);
    void save(Training trainings);
    List<Training> getAll();
    List<Training> match(Criteria criteria);
    Training matchOne(Criteria criteria);
    List<Training> findByStartDate(StartDate date);
    Optional<Training> findById(String id);
}
