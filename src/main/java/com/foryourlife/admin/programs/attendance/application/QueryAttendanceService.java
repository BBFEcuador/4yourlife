package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryAttendanceService {
    private final AttendanceRepository _attendanceRepository;

    public QueryAttendanceService(AttendanceRepository _attendanceRepository) {
        this._attendanceRepository = _attendanceRepository;
    }

    public List<Attendance> getAttendancesByUser(String userId) {
        return _attendanceRepository.findAttendanceByUser(userId);
    }

    public List<Attendance> getAttendancesByTraining(String trainingId) {
        return _attendanceRepository.findAttendanceByTraining(trainingId);
    }
}
