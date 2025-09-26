package com.foryourlife.admin.programs.attendance.infraestructure.httpController;

import com.foryourlife.admin.programs.attendance.application.CommandAttendanceService;
import com.foryourlife.admin.programs.attendance.application.QueryAttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    private CommandAttendanceService _commandAttendanceService;
    private QueryAttendanceService _queryAttedanceService;

    public AttendanceController(CommandAttendanceService _commandAttendanceService, QueryAttendanceService _queryAttedanceService) {
        this._commandAttendanceService = _commandAttendanceService;
        this._queryAttedanceService = _queryAttedanceService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAttendancesByUser(@PathVariable String userId) {
        return new ResponseEntity<>(_queryAttedanceService.getAttendancesByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<?> getAttendancesByTraining(@PathVariable String trainingId) {
        return new ResponseEntity<>(_queryAttedanceService.getAttendancesByTraining(trainingId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> saveAttendance(@Valid @RequestBody SaveAttendanceRequest request) {
        _commandAttendanceService.saveAttendance(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("close-attendance/{id}")
    public ResponseEntity<?> closeAttendance(@PathVariable String id) {
        _commandAttendanceService.closeAttendance(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
