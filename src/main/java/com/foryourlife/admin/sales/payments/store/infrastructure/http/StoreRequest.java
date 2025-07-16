package com.foryourlife.admin.sales.payments.store.infrastructure.http;

import com.foryourlife.admin.programs.campus.domain.Campus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StoreRequest {
    public String id;
    @NotNull(message = "El campo direccion es requerido")
    @NotBlank(message = "El campo direccion no puede estar vacio")
    public String address;
    @NotNull(message = "El campo numero de establecimiento es requerido")
    @NotBlank(message = "El campo numero de establecimiento no puede estar vacio")
    public String number;
    @NotNull(message = "El campusId es requerido")
    @NotBlank(message = "El campusId no puede estar vacio")
    public String campusId;

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getCampusId() {
        return campusId;
    }
}
