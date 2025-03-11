package com.foryourlife.visionary.infrastructure.httpControllers;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.visionary.application.VisionaryCreatorService;
import com.foryourlife.visionary.application.VisionaryFinderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("visionary")
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
        return new ResponseEntity<>(finderService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getVisionaryByUserId(@PathVariable String userId) {
        return finderService.getById(userId).map(visionary -> new ResponseEntity<>(visionary, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteVisionary(@PathVariable String id) {
        creatorService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/visionary-available/{lvl}")
    public ResponseEntity<?> checkVisionaryAvailable(@PathVariable String lvl) {
        switch (lvl) {
            case "FOCUS" -> {
                var criteria = new Criteria(List.of(new Filter("courseLevel", CourseLevel.FOCUS.toString(), "participantLevel", Filter.Operation.EQUAL, Filter.LogicalOperator.AND), new Filter("teams", null, null, Filter.Operation.IS_EMPTY, Filter.LogicalOperator.AND)), Optional.empty(), Optional.empty());
                return new ResponseEntity<>(finderService.match(criteria), HttpStatus.OK);
            }
            case "YOUR" -> {
                var criteria = new Criteria(List.of(new Filter("courseLevel", CourseLevel.YOUR.toString(), "participantLevel", Filter.Operation.EQUAL, Filter.LogicalOperator.AND), new Filter("teams", null, null, Filter.Operation.IS_EMPTY, Filter.LogicalOperator.AND)), Optional.empty(), Optional.empty());
                return new ResponseEntity<>(finderService.match(criteria), HttpStatus.OK);
            }
            case "LIFE" -> {
                var criteria = new Criteria(List.of(new Filter("courseLevel", CourseLevel.LIFE.toString(), "participantLevel", Filter.Operation.EQUAL, Filter.LogicalOperator.AND), new Filter("teams", null, null, Filter.Operation.IS_EMPTY, Filter.LogicalOperator.AND)), Optional.empty(), Optional.empty());
                return new ResponseEntity<>(finderService.match(criteria), HttpStatus.OK);
            }
            default -> throw new BaseException("Illegal argument", List.of("Type must be FOCUS, YOUR or LIFE"));
        }
    }

    @PostMapping("/visionary-admin")
    public ResponseEntity<?> createFromAdmin(@Valid @RequestBody VisionaryAdminRequest request) {
        creatorService.createFromAdmin(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
