package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.attendance.infraestructure.httpController.SaveAttendanceRequest;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        LocalDate today = LocalDate.now();
        LocalDate start = att.getTraining().getStartDate();
        LocalDate end = att.getTraining().getEndDate();

        long dayNumber = ChronoUnit.DAYS.between(start, today) + 1;

        switch (attendance.day) {
            case FRIDAY -> {
                if (dayNumber == 1) {
                    att.setFridayAttendance(attendance.attendanceStatus);
                } else{
                    throw new BaseException("Solo se puede registrar la asistencia del viernes el primer día del entrenamiento", List.of());
                }
            }
            case SATURDAY -> {
                if (dayNumber == 2) {
                    att.setSaturdayAttendance(attendance.attendanceStatus);
                } else{
                    throw new BaseException("Solo se puede registrar la asistencia del sábado el segundo día del entrenamiento", List.of());
                }
            }
            case SUNDAY -> {
                if (dayNumber == 3) {
                    att.setSundayAttendance(attendance.attendanceStatus);
                } else{
                    throw new BaseException("Solo se puede registrar la asistencia del domingo el tercer día del entrenamiento", List.of());
                }
            }
        }
        _attendanceRepository.save(att);
        if (att.HasUnAttendance()) {
            var event = att.pullDomainEvents();
            this.bus.publish(event);
        }
    }

    public void closeAttendance(String id) {
        _attendanceRepository.findAttendanceByTraining(id).forEach(attendance -> {
            attendance.setActive(false);
            if (attendance.getFridayAttendance() == null) attendance.setFridayAttendance(AttendanceStatus.ASISTIO);
            if (attendance.getSaturdayAttendance() == null) attendance.setSaturdayAttendance(AttendanceStatus.ASISTIO);
            if (attendance.getSundayAttendance() == null) attendance.setSundayAttendance(AttendanceStatus.ASISTIO);
            if (attendance.HasUnAttendance()) {
                this.bus.publish(attendance.pullDomainEvents());
            }
            _attendanceRepository.save(attendance);
        });
    }
}
