package com.foryourlife.visionary.infrastructure.httpControllers;

import com.foryourlife.visionary.application.VisionaryCreatorService;
import com.foryourlife.visionary.application.VisionaryFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("visionary"
)
public class VisionaryController {
    @Autowired
    private VisionaryFinderService finderService;
    @Autowired
    private VisionaryCreatorService creatorService;

    @PostMapping("add")
    public ResponseEntity<?> createVisionary(@RequestBody VisionaryRequest visionaryRequest) {
        creatorService.create(visionaryRequest.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getVisionaries() {
        return new ResponseEntity<>(finderService.getAll(),HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getVisionaryByUserId(@PathVariable String userId) {
        return finderService.getById(userId)
               .map(visionary -> new ResponseEntity<>(visionary, HttpStatus.OK))
               .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteVisionary(@PathVariable String id) {
        creatorService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
