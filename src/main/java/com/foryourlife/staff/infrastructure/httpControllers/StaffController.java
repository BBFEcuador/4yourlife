package com.foryourlife.staff.infrastructure.httpControllers;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.staff.application.StaffCreatorService;
import com.foryourlife.staff.application.StaffFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("staff")
public class StaffController {
    @Autowired
    private StaffCreatorService staffService;

    @Autowired
    private StaffFinderService staffFinderService;

    @PostMapping("add")
    public ResponseEntity<?> createStaff(@RequestBody StaffRequest staff) {
        staffService.create(staff.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("")
    public ResponseEntity<?> findStaffByUserId() {
        return new ResponseEntity<>(staffFinderService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findStaffById(@PathVariable String id) {
        return new ResponseEntity<>(staffFinderService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable String id) {
        staffService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/staff-available/{lvl}")
    public ResponseEntity<?> getForTeam(@PathVariable String lvl) {
        switch (lvl) {
            case "FOCUS" -> {
                var criteria = new Criteria(
                        List.of(new Filter(
                                "courseLevel",
                                CourseLevel.FOCUS.toString(),
                                "participantLevel",
                                Filter.Operation.EQUAL,
                                Filter.LogicalOperator.AND
                        ), new Filter(
                                "teams",
                                null,
                                null,
                                Filter.Operation.IS_EMPTY,
                                Filter.LogicalOperator.AND
                        )), Optional.empty(), Optional.empty()
                );
                return new ResponseEntity<>(staffFinderService.match(criteria), HttpStatus.OK);
            }
            case "YOUR" -> {
                var criteria = new Criteria(
                        List.of(new Filter(
                                "courseLevel",
                                CourseLevel.YOUR.toString(),
                                "participantLevel",
                                Filter.Operation.EQUAL,
                                Filter.LogicalOperator.AND
                        ), new Filter(
                                "teams",
                                null,
                                null,
                                Filter.Operation.IS_EMPTY,
                                Filter.LogicalOperator.AND
                        )), Optional.empty(), Optional.empty()
                );
                return new ResponseEntity<>(staffFinderService.match(criteria), HttpStatus.OK);
            }
            case "LIFE" -> {
                var criteria = new Criteria(
                        List.of(new Filter(
                                "courseLevel",
                                CourseLevel.LIFE.toString(),
                                "participantLevel",
                                Filter.Operation.EQUAL,
                                Filter.LogicalOperator.AND
                        ), new Filter(
                                "teams",
                                null,
                                null,
                                Filter.Operation.IS_EMPTY,
                                Filter.LogicalOperator.AND
                        )), Optional.empty(), Optional.empty()
                );
                return new ResponseEntity<>(staffFinderService.match(criteria), HttpStatus.OK);
            }
            default -> throw new BaseException("Illegal argument", List.of("Type must be FOCUS, YOUR or LIFE"));
        }
    }
}
