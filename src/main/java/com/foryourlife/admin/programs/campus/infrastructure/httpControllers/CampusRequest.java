package com.foryourlife.admin.programs.campus.infrastructure.httpControllers;

import com.foryourlife.admin.programs.campus.domain.Campus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class CampusRequest {
    public String id;
    @NotNull
    @NotBlank(message = "El país es requerido")
    public String country;
    @NotNull
    @NotBlank(message = "La ciudad es requerida")
    public String city;
    @NotNull
    @NotBlank(message = "La dirección es requerida")
    public String address;
    @NotNull
    @NotBlank(message = "El numero de telefono es requerido")
    @Pattern(regexp = "^[0-9]*$", message = "El telefono solo puede contener digitos")
    public String phone;

    public CampusRequest(String id, String country, String city, String address, String phone) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.address = address;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Campus toDomain() {
        return Campus.create(id != null ? id : UUID.randomUUID().toString(), this.country, this.city, this.address, this.phone);
    }
}
