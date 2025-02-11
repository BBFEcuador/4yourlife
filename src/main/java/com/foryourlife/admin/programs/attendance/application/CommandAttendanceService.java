package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.shared.domain.bus.EventBus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandAttendanceService {
    private final AttendanceRepository _attendanceRepository;
    private EventBus bus;

    public CommandAttendanceService(AttendanceRepository _attendanceRepository, EventBus bus) {
        this._attendanceRepository = _attendanceRepository;
        this.bus = bus;
    }

    public void saveAttendance(Attendance attendance) {
        _attendanceRepository.save(attendance);
        if (attendance.HasUnAttendance()) {
            this.bus.publish(attendance.pullDomainEvents());
        }
    }

    public void saveAttendance(List<Attendance> attendances) {
        _attendanceRepository.saveAll(attendances);
    }
}
