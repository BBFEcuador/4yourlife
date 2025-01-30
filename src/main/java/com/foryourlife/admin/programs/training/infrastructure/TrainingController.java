package com.foryourlife.admin.programs.training.infrastructure;

import com.foryourlife.admin.programs.training.application.CommandTrainingService;
import com.foryourlife.admin.programs.training.application.QueryTrainingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'USER')")
    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(queryTrainingService.getAllTrainings(), HttpStatus.OK);
    }
}
