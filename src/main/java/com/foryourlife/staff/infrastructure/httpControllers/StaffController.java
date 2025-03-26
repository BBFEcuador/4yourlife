package com.foryourlife.staff.infrastructure.httpControllers;

import com.foryourlife.admin.programs.trainer.infrastructure.httpControllers.AvailableTrainerRequest;
import com.foryourlife.staff.application.StaffCreatorService;
import com.foryourlife.staff.application.StaffFinderService;
import jakarta.validation.Valid;
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

    @PutMapping("add")
    public ResponseEntity<?> updateStaff(@RequestBody StaffRequest staff) {
        staffService.update(staff.toDomain());
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

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable String id) {
        staffService.changeStatus(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/staff-available")
    public ResponseEntity<?> getForTeam(@Valid @RequestBody AvailableTrainerRequest request) {
        return new ResponseEntity<>(staffFinderService.findAvailableStaff(request.startDate, request.endDate), HttpStatus.OK);
    }

    @PostMapping("/staff-admin")
    public ResponseEntity<?> createFromAdmin(@Valid @RequestBody StaffUserRequest request){
            staffService.createFromAdmin(request.toDomain());
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/staff-participant")
    public ResponseEntity<?> createFromParticipant(@Valid @RequestBody ParticipantTypeRequest request){
            staffService.createFromParticipant(request.userId, request.role);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
