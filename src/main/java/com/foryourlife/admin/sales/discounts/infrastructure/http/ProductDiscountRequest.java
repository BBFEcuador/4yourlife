package com.foryourlife.admin.sales.discounts.infrastructure.http;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ProductDiscountRequest {
    public String id;
    @NotNull(message = "El nombre es requerido")
    @NotEmpty(message = "El nombre no puede estar vacío")
    public String name;
    @NotNull(message = "El tipo de descuento es requerido")
    @NotEmpty(message = "El tipo de descuento no puede estar vacío")
    @Pattern(regexp = "^[PE]$", message = "el tipo debe ser P (porcentaje) o E (efectivo)")
    public String discountType;
    @NotNull(message = "El valor del descuento es requerido")
    @NotEmpty(message = "El valor del descuento no puede estar vacío")
    @Pattern(message = "El valor del descuento debe ser un número", regexp = "^[0-9]+(\\.[0-9]+)?$")
    public String discountValue;
    public Boolean needSupervision = false;
    public Boolean isActive = true;
}
