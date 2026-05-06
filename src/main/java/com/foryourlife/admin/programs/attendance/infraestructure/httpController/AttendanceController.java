package com.foryourlife.admin.programs.attendance.infraestructure.httpController;

import com.foryourlife.admin.programs.attendance.application.AttendanceCommandService;
import com.foryourlife.admin.programs.attendance.application.QueryAttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceCommandService _AttendanceCommandService;
    private final QueryAttendanceService _queryAttedanceService;

    public AttendanceController(AttendanceCommandService _AttendanceCommandService, QueryAttendanceService _queryAttedanceService) {
        this._AttendanceCommandService = _AttendanceCommandService;
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
        _AttendanceCommandService.saveAttendance(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("close-attendance/{id}")
    public ResponseEntity<?> closeAttendance(@PathVariable String id) {
        _AttendanceCommandService.closeAttendance(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
