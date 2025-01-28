package com.foryourlife.admin.programs.training.domain;

import java.util.List;
import java.util.Optional;

public interface TrainingRepository {
    void saveAll(List<Training> trainings);
    void save(Training trainings);
    List<Training> getAll();
    Optional<Training> findById(String id);
}
