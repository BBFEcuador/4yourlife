package com.foryourlife.admin.auth.infrastructure.httpControllers;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.programs.campus.domain.Campus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class CreateAdminRequest {
    public String id;
    @NotNull
    @NotBlank(message = "El primer nombre es requerido")
    public String name1;
    @NotNull
    @NotBlank(message = "El segundo nombre es requerido")
    public String name2;
    @NotNull
    @NotBlank(message = "El primer apellido es requerido")
    public String lastname1;
    @NotNull
    @NotBlank(message = "El segundo apellido es requerido")
    public String lastname2;
    @NotNull
    @Email
    public String email;
    @NotNull
    public AdminRole role;
    @NotNull
    public String phone;
    @NotNull
    public Set<Campus> campus;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public AdminRole getRole() {
        return role;
    }

    public Set<Campus> getCampus() {
        return campus;
    }
}
