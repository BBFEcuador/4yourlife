package com.foryourlife.clients.account.promises.infrastructure.http;

import com.foryourlife.admin.programs.attendance.infraestructure.httpController.DaysEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PromiseRequest {
    @NotNull(message = "El id de la declaracion no puede ser nulo")
    public String id;
    @NotNull(message = "El valor de la declaracion no puede ser nulo")
    public Integer promise;
    @NotNull(message = "El día al que corresponde la asistencia es requerido")
    @NotEmpty(message = "El día al que correpsonde la asistencia no puede estar vacío")
    public String day;
}
