package com.foryourlife.admin.programs.attendance.infraestructure.httpController;

import com.foryourlife.admin.programs.attendance.application.CommandAttendanceService;
import com.foryourlife.admin.programs.attendance.application.QueryAttendanceService;
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
    public ResponseEntity<?> getAttendancesByUser(String userId) {
        return new ResponseEntity<>(_queryAttedanceService.getAttendancesByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<?> getAttendancesByTraining(String trainingId) {
        return new ResponseEntity<>(_queryAttedanceService.getAttendancesByTraining(trainingId),HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveAttendance(@RequestBody SaveAttendanceRequest request) {
        _commandAttendanceService.saveAttendance(request.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/saveAll")
    public ResponseEntity<?> saveAttendance(@RequestBody List<SaveAttendanceRequest> request) {
        _commandAttendanceService.saveAttendance(request.stream().map(SaveAttendanceRequest::toDomain).toList());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
