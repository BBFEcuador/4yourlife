package com.foryourlife.admin.programs.training.infrastructure.httpControllers;

import com.foryourlife.admin.programs.training.application.CommandTrainingService;
import com.foryourlife.admin.programs.training.application.QueryTrainingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/training")
public class TrainingController {
    private final CommandTrainingService commandTrainingService;
    private final QueryTrainingService queryTrainingService;

    public TrainingController(CommandTrainingService commandTrainingService, QueryTrainingService queryTrainingService) {
        this.commandTrainingService = commandTrainingService;
        this.queryTrainingService = queryTrainingService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'USER')")
    @PostMapping("generate")
    public ResponseEntity<?> autoGenerate(@Valid @RequestBody TrainingAutoGenerateRequest request) {
        commandTrainingService.autoGenerateTraining(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'USER')")
    @PutMapping("update")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateTrainingRequest request) {
        if (queryTrainingService.getTrainingById(request.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        commandTrainingService.saveTraining(request.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/date")
    public ResponseEntity<?> updateStartDate(@Valid @RequestBody UpdateStartDateTrainingRequest request) {
        if (queryTrainingService.getTrainingById(request.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        commandTrainingService.updateStartDate(request.getId(),request.getStartDate());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'USER')")
    @GetMapping("/calendar-event")
    public ResponseEntity<?> getAllCalendarEvent() {
        var trainings = queryTrainingService.getAllTrainings();
        List<TrainingResponse> trainingResponse = new ArrayList<>();
        trainings.forEach(training -> {
            String color = switch (training.getCourseLevel()) {
                                        case FOCUS -> "#2f49f5";
                                        case YOUR -> "#f52f9c";
                                        case LIFE -> "#f5922f";
                                        default -> "#00FF00";
            };
            trainingResponse.add(
                    new TrainingResponse(training.getId(), training.getName() + " " + training.getCourseLevel().name(),training.getStartDate(),training.getEndDate().atTime(23,59),false,color, new TrainingResponse.ExtendedProps("Epic description","Nice location",new String[]{"clave","valor"}))
            );
        });
        return new ResponseEntity<>(trainingResponse, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(queryTrainingService.getAllTrainings(), HttpStatus.OK);
    }
}
