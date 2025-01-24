package com.foryourlife.admin.training.program.application;

import com.foryourlife.admin.training.program.domain.TrainingRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommandTrainingService {

    private final TrainingRepository repository;

    public CommandTrainingService(TrainingRepository repository) {
        this.repository = repository;
    }

    public void autoGenerateTraining(Date startDate, Integer firstFocus, Integer numberOfFocus, String campusId) {

    }
}
