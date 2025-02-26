package com.foryourlife.admin.programs.trainer.infrastructure.httpControllers;

import com.foryourlife.admin.auth.infrastructure.httpControllers.DisableAdminRequest;
import com.foryourlife.admin.programs.trainer.application.TrainerCreatorService;
import com.foryourlife.admin.programs.trainer.application.TrainerFinderService;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private TrainerFinderService trainerFinderService;

    @Autowired
    private TrainerCreatorService trainerCreateService;

    @PostMapping("")
    public ResponseEntity<?> createTrainer(@Valid @RequestBody TrainerRequest request) {
        trainerCreateService.createTrainer(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<?> updateTrainer(@RequestBody TrainerRequest request) {
        trainerCreateService.updateTrainer(request.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        return new ResponseEntity<>(trainerFinderService.findTrainers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Trainer>> getTrainerById(@PathVariable String id) {
        return new ResponseEntity<>(trainerFinderService.findTrainerById(id), HttpStatus.OK);
    }

    @PutMapping("/disabled")
    public ResponseEntity<?> disableAdmin(@RequestBody DisableTrainerRequest disabled){
        trainerCreateService.update(disabled);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
