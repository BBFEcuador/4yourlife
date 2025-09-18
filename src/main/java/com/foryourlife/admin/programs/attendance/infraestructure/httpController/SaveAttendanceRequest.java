package com.foryourlife.admin.programs.attendance.infraestructure.httpController;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import com.foryourlife.admin.programs.attendance.domain.FylStage;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.clients.account.participant.domain.Participant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class SaveAttendanceRequest {
    @NotNull(message = "El id no debe ser nulo")
    @NotEmpty(message = "El id no debe estar vacío")
    public String id;
    @NotNull(message = "El estado de asistencia es requerido")
    @NotEmpty(message = "El estado de asistencia no puede estar vacío")
    public AttendanceStatus attendanceStatus;
    @NotNull(message = "El día al que corresponde la asistencia es requerido")
    @NotEmpty(message = "El día al que correpsonde la asistencia no puede estar vacío")
    public DaysEnum day;
}
