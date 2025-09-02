package com.foryourlife.admin.bank.infrastructure.http;

import jakarta.validation.constraints.NotNull;

public class BankRequest {
    private String id;
    @NotNull(message = "El nombre es requerido")
    private String name;
    @NotNull(message = "El numero de cuenta es requerido")
    private String number;
    @NotNull(message = "El campus es requerido")
    private String campusId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }
}
