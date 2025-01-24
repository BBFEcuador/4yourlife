package com.foryourlife.admin.training.program.domain;

import java.util.List;

public interface TrainingRepository {
    void saveAll(List<Training> trainings);
    void save(Training trainings);
    List<Training> getAll();
}
