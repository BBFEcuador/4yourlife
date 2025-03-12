package com.foryourlife.visionary.infrastructure.httpControllers;

import com.foryourlife.admin.programs.trainer.infrastructure.httpControllers.AvailableTrainerRequest;
import com.foryourlife.visionary.application.VisionaryCreatorService;
import com.foryourlife.visionary.application.VisionaryFinderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("visionary")
public class VisionaryController {
    @Autowired
    private VisionaryFinderService finderService;
    @Autowired
    private VisionaryCreatorService creatorService;

    @PostMapping("add")
    public ResponseEntity<?> createVisionary(@Valid @RequestBody VisionaryRequest visionaryRequest) {
        creatorService.create(visionaryRequest.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getVisionaries() {
        return new ResponseEntity<>(finderService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getVisionaryByUserId(@PathVariable String userId) {
        return finderService.getById(userId)
                .map(visionary -> new ResponseEntity<>(visionary, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/visionaries-available")
    public ResponseEntity<?> getForTeam(@Valid @RequestBody AvailableTrainerRequest request) {
        return new ResponseEntity<>(finderService.findAvailableVisionaries(request.startDate, request.endDate), HttpStatus.OK);
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable String id) {
        creatorService.changeStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
