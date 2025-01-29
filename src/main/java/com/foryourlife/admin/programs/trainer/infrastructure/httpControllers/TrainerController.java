package com.foryourlife.admin.programs.trainer.infrastructure.httpControllers;

import com.foryourlife.admin.programs.trainer.application.TrainerCreatorService;
import com.foryourlife.admin.programs.trainer.application.TrainerFinderService;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/add")
    public void createTrainer(@RequestBody TrainerRequest request, HttpServletResponse httpServletResponse) {
        trainerCreateService.createTrainer(request.toDomain());
    }

    @GetMapping("")
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        return new ResponseEntity<>(trainerFinderService.findTrainers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Trainer>> getTrainerById(@PathVariable String id) {
        return new ResponseEntity<>(trainerFinderService.findTrainerById(id), HttpStatus.OK);
    }
}
