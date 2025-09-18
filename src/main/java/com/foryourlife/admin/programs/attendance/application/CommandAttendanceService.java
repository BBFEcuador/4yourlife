package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.attendance.infraestructure.httpController.SaveAttendanceRequest;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
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

    public void saveAttendance(SaveAttendanceRequest attendance) {
        var att = _attendanceRepository.findById(attendance.id).orElseThrow(() -> new BaseException("No se encontro el registro especificado", List.of()));
        switch (attendance.day) {
            case FRIDAY -> {
                att.setFridayAttendance(attendance.attendanceStatus);
            }
            case SATURDAY -> {
                att.setSaturdayAttendance(attendance.attendanceStatus);
            }
            case SUNDAY -> {
                att.setSundayAttendance(attendance.attendanceStatus);
            }
        }
        if (att.HasUnAttendance()) {
            this.bus.publish(att.pullDomainEvents());
        }
    }

    public void closeAttendance(String id) {
        _attendanceRepository.findAttendanceByTraining(id).forEach(attendance -> {
            attendance.setActive(false);
            if (attendance.getFridayAttendance() == null) attendance.setFridayAttendance(AttendanceStatus.NO_ASISTIO);
            if (attendance.getSaturdayAttendance() == null)
                attendance.setSaturdayAttendance(AttendanceStatus.NO_ASISTIO);
            if (attendance.getSundayAttendance() == null) attendance.setSundayAttendance(AttendanceStatus.NO_ASISTIO);
            if (attendance.HasUnAttendance()) {
                this.bus.publish(attendance.pullDomainEvents());
            }
            _attendanceRepository.save(attendance);
        });
    }
}
