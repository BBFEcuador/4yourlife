package com.foryourlife.staff.infrastructure.httpControllers;

import com.foryourlife.staff.application.StaffCreatorService;
import com.foryourlife.staff.application.StaffFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("{userId}")
    public ResponseEntity<?> findStaffByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(staffFinderService.findByUserId(userId), HttpStatus.OK);
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
}
