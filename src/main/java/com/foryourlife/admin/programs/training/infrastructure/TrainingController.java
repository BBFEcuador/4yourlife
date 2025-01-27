package com.foryourlife.admin.programs.training.infrastructure;

import com.foryourlife.admin.programs.training.application.CommandTrainingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/training")
public class TrainingController {
    private final CommandTrainingService commandTrainingService;

    public TrainingController(CommandTrainingService commandTrainingService) {
        this.commandTrainingService = commandTrainingService;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'USER')")
    @PostMapping("generate")
    public ResponseEntity<?> autoGenerate(@Valid @RequestBody TrainingAutoGenerateRequest request) {
        commandTrainingService.autoGenerateTraining(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
