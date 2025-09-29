package com.foryourlife.admin.programs.attendance.infraestructure.httpController;

import com.foryourlife.admin.programs.attendance.domain.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public class SaveAttendanceRequest {
    @NotNull(message = "El id no debe ser nulo")
    @UUID(message = "El id debe ser valido")
    public String id;
    @NotNull(message = "El estado de asistencia es requerido")
    public AttendanceStatus attendanceStatus;
    @NotNull(message = "El día al que corresponde la asistencia es requerido")
    @NotBlank(message = "El día al que correpsonde la asistencia no puede estar vacío")
    public String day;
}
