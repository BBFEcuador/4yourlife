package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandAttendanceService {
    private final AttendanceRepository _attendanceRepository;

    public CommandAttendanceService(AttendanceRepository _attendanceRepository) {
        this._attendanceRepository = _attendanceRepository;
    }

    public void saveAttendance(Attendance attendance) {
        _attendanceRepository.save(attendance);
    }

    public void saveAllAttendance(List<Attendance> attendances) {
        _attendanceRepository.saveAll(attendances);
    }
}
