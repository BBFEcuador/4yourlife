package com.foryourlife.admin.programs.trainer.infrastructure.httpControllers;

import com.foryourlife.admin.programs.trainer.application.TrainerCreatorService;
import com.foryourlife.admin.programs.trainer.application.TrainerQueryService;
import com.foryourlife.admin.programs.trainer.trainerDashboard.application.TrainerViewQueryService;
import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.trainer.trainerDashboard.domain.life.TrainerLifeView;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("trainer")
public class TrainerController {

    @Autowired
    private TrainerQueryService trainerQueryService;

    @Autowired
    private TrainerCreatorService trainerCreateService;

    @Autowired
    private TrainerViewQueryService trainerViewQueryService;

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
    public ResponseEntity<?> getAllTrainers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {
        var p = PageRequest.of(page, perPage, Sort.by("id").descending());
        Criteria criteria = new Criteria(
                List.of(), Optional.empty(), Optional.empty()
        );
        if (!search.isEmpty()) {
            criteria.filters = List.of(
                    new Filter(
                            "name",
                            search,
                            null,
                            Filter.Operation.LIKE,
                            Filter.LogicalOperator.OR
                    ),
                    new Filter(
                            "email",
                            search,
                            null,
                            Filter.Operation.LIKE,
                            Filter.LogicalOperator.OR
                    ),
                    new Filter(
                            "phone",
                            search,
                            null,
                            Filter.Operation.LIKE,
                            Filter.LogicalOperator.OR
                    ),
                    new Filter(
                            "name",
                            search,
                            "teams",
                            Filter.Operation.LIKE,
                            Filter.LogicalOperator.OR
                    )
            );
        }
        return new ResponseEntity<>(trainerQueryService.findTrainers(p, criteria), HttpStatus.OK);
    }

    @PostMapping("/available")
    public ResponseEntity<List<Trainer>> getTrainers(@Valid @RequestBody AvailableTrainerRequest request) {
        return new ResponseEntity<>(trainerQueryService.findTrainersAvailable(request.startDate, request.endDate), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Trainer>> getTrainerById(@PathVariable String id) {
        return new ResponseEntity<>(trainerQueryService.findTrainerById(id), HttpStatus.OK);
    }

    @PutMapping("/disabled")
    public ResponseEntity<?> disableAdmin(@RequestBody DisableTrainerRequest disabled) {
        trainerCreateService.update(disabled);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/view/life/{id}")
    public ResponseEntity<List<TrainerLifeView>> getTrainerViewById(@PathVariable String id) {
        return new ResponseEntity<>(trainerViewQueryService.getTrainerView(id), HttpStatus.OK);
    }
}
