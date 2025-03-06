package com.foryourlife.admin.programs.training.application;

import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import com.foryourlife.admin.programs.training.domain.EndDate;
import com.foryourlife.admin.programs.training.domain.StartDate;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.admin.programs.training.infrastructure.httpControllers.TrainingAutoGenerateRequest;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.foryourlife.shared.domain.level.CourseLevel.*;

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
        Integer nextNumber = 1;
        if (request.firstFocus != null){
            nextNumber = request.firstFocus;
        }else{
            Training lastTraining = repository.matchOne(
                    new Criteria(
                            List.of(
                                    new Filter(
                                            "number", null, null, Filter.Operation.GET_LAST, null
                                    )
                            ), Optional.empty(), Optional.empty()
                    )
            );

            if (lastTraining == null) {
                throw new BaseException("No previous training found", List.of("Cannot determine the next training number"));
            }

            nextNumber = lastTraining.getNumber() + 1;
        }
        Criteria ctriCriteria = new Criteria(
                List.of(
                        new Filter(
                                "startDate",
                                request.startDate.toString(),
                                null,
                                Filter.Operation.EQUAL,
                                Filter.LogicalOperator.AND
                        )
                ), Optional.empty(), Optional.empty()
        );
        repository.findByStartDate(new StartDate(request.startDate)).forEach(training -> {
            if (training.getCourseLevel() != LIFE) {
                throw new BaseException("Error al actualizar la fecha", List.of("Entrenamientos Focus o Your no pueden coincidir"));
            }
        });
        var focus = Training.create(
                UUID.randomUUID().toString(),
                request.firstFocus != null ? request.firstFocus : nextNumber,
                campus.getCity() + "-" + (request.firstFocus != null ? request.firstFocus.toString() : nextNumber.toString()),
                request.startDate,
                request.startDate.plusDays(2),
                FOCUS,
                null,
                campus,
                true
        );

        var trainingList = focus.generateNextLevel(request.numberOfFocus);
        this.repository.saveAll(trainingList);
    }

    public void saveTraining(Training training) {
        this.repository.save(training);
    }

    public void updateStartDate(String id, LocalDate date) {
        Criteria ctriCriteria = new Criteria(
                List.of(
                        new Filter(
                                "startDate",
                                date.toString(),
                                null,
                                Filter.Operation.EQUAL,
                                Filter.LogicalOperator.AND
                        )
                ), Optional.empty(), Optional.empty()
        );
        repository.findByStartDate(new StartDate(date)).forEach(training -> {
            if (training.getCourseLevel() != LIFE) {
                throw new BaseException("Error al actualizar la fecha", List.of("Entrenamientos Focus o Your no pueden coincidir"));
            }
        });
        var training = this.repository.findById(id).get();
        training.setStartDate(new StartDate(date));
        training.setEndDate(new EndDate(date.plusDays(2)));
        this.repository.save(training);
    }
}
