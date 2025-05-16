package com.foryourlife.admin.sales.discounts.infrastructure.http;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ChangeStatusRequest {
    @NotNull(message = "El id es requerido")
    @NotEmpty(message = "El id no puede estar vacío")
    public String id;
    @NotNull(message = "El estado es requerido")
    public Boolean status;
}
