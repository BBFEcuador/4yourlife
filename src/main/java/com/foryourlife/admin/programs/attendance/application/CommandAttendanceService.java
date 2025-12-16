package com.foryourlife.admin.programs.attendance.application;

import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.attendance.infraestructure.httpController.DaysEnum;
import com.foryourlife.admin.programs.attendance.infraestructure.httpController.SaveAttendanceRequest;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.admin.programs.training.application.TrainingValidationService;
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

    public void saveAttendance(SaveAttendanceRequest attendanceRequest) {

        var attendance = _attendanceRepository.findById(attendanceRequest.id)
                .orElseThrow(() -> new BaseException(
                        "Registro no encontrado",
                        List.of("El ID proporcionado no corresponde a ningún registro de asistencia.")
                ));

        DaysEnum dayEnum = DaysEnum.fromString(attendanceRequest.day);

        switch (dayEnum) {
            case FRIDAY -> {
                    attendance.setFridayAttendance(attendanceRequest.attendanceStatus);
                    if (attendance.getFridayAttendance().equals(AttendanceStatus.ASISTIO)) {
                        attendance.setSaturdayAttendance(AttendanceStatus.ASISTIO);
                        attendance.setSundayAttendance(AttendanceStatus.ASISTIO);
                    }else if (attendance.getFridayAttendance().equals(AttendanceStatus.NO_ASISTIO)) {
                        attendance.setSaturdayAttendance(AttendanceStatus.DESERTO);
                        attendance.setSundayAttendance(AttendanceStatus.DESERTO);
                    }
            }
            case SATURDAY -> {
                attendance.setSaturdayAttendance(attendanceRequest.attendanceStatus);
                if (attendance.getSaturdayAttendance().equals(AttendanceStatus.ASISTIO)) {
                    attendance.setSundayAttendance(AttendanceStatus.ASISTIO);
                }else if (attendance.getSaturdayAttendance().equals(AttendanceStatus.NO_ASISTIO) || attendance.getSaturdayAttendance().equals(AttendanceStatus.DESERTO)) {
                    attendance.setSundayAttendance(AttendanceStatus.DESERTO);
                }
            }
            case SUNDAY -> attendance.setSundayAttendance(attendanceRequest.attendanceStatus);
            default -> throw new BaseException(
                    "El día seleccionado no coincide con el día del entrenamiento",
                    List.of("El día proporcionado no es válido para registrar asistencia.")
            );
        }

        _attendanceRepository.save(attendance);

        if (attendance.HasUnAttendance()) {
            var events = attendance.pullDomainEvents();
            bus.publish(events);
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
