package com.foryourlife.admin.programs.training.application;

import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.programs.training.infrastructure.TrainingAutoGenerateRequest;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommandTrainingService {

    private final TrainingRepository repository;
    private final QueryCampusService campusService;

    public CommandTrainingService(TrainingRepository repository, QueryCampusService campusService) {
        this.repository = repository;
        this.campusService = campusService;
    }

    public void autoGenerateTraining(TrainingAutoGenerateRequest request) {
        var campus = campusService.findById(request.campusId);
        var focus = Training.create(
                UUID.randomUUID().toString(),
                request.firstFocus,
                campus.getCity() + "-" + request.firstFocus,
                request.startDate,
                request.startDate.plusDays(2),
                CourseLevel.FOCUS,
                null,
                campus,
                true
        );
        var trainingList = focus.generateNextLevel(request.numberOfFocus);
        this.repository.saveAll(trainingList);
    }
}
