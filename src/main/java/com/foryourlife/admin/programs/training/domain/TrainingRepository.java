package com.foryourlife.admin.programs.training.domain;

import java.util.List;

public interface TrainingRepository {
    void saveAll(List<Training> trainings);
    void save(Training trainings);
    List<Training> getAll();
}
