package com.foryourlife.admin.sales.payments.paymentMethod.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class PaymentMethodRequest {
    public String id;
    @NotNull(message = "El campo tipo es requerido")
    public String type;
    @NotNull(message = "El campo estado es requerido")
    @NotNull(message = "El campo código es requerido")
    @Pattern(regexp = "^(EF|CQ|TC|TRA)$", message = "El tipo debe ser EF, CQ, TC o TRA")
    public String code;
    public Boolean isActive;
    @NotNull(message = "El id del campus es requerido")
    public String campusId;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Boolean getActive() {
        return isActive;
    }

    public PaymentMethod toDomain() {
        return PaymentMethod.create(id != null ? id: UUID.randomUUID().toString(), type, isActive, code, null);
    }
}