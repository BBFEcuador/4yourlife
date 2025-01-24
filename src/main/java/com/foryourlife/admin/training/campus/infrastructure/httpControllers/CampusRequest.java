package com.foryourlife.admin.training.campus.infrastructure.httpControllers;

import com.foryourlife.admin.training.campus.domain.Campus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class CampusRequest {
    public String id;
    @NotNull
    @NotBlank(message = "The country field is required")
    public String country;
    @NotNull
    @NotBlank(message = "The city field is required")
    public String city;
    @NotNull
    @NotBlank(message = "The address field is required")
    public String address;
    @NotNull
    @NotBlank(message = "The phone field is required")
    @Pattern(regexp = "^[0-9]*$", message = "The phone field only accept digits")
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
