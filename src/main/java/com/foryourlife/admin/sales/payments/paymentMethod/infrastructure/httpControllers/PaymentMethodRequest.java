package com.foryourlife.admin.sales.payments.paymentMethod.infrastructure.httpControllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class PaymentMethodRequest {
    public String id;
    @NotNull(message = "El campo tipo es requerido")
    public String type;
    @NotBlank(message = "El campo código es requerido")
    @NotNull(message = "El campo código es requerido")
    @Pattern(regexp = "^(EF|CQ|TC|TRA)$", message = "El tipo debe ser EF, CQ, TC o TRA")
    public String code;
    public Boolean isActive;
    @NotNull(message = "El id del campus es requerido")
    public String campusId;
    public String bankId;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Boolean getActive() {
        return isActive;
    }
}