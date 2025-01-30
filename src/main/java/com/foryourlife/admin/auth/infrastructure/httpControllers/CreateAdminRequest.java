package com.foryourlife.admin.auth.infrastructure.httpControllers;

import com.foryourlife.admin.auth.domain.Admin;
import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.programs.campus.domain.Campus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class CreateAdminRequest {
    public String id;
    @NotNull
    public String name;
    @NotNull
    @Email
    public String email;
    @NotNull
    public AdminRole role;
    @NotNull
    public Set<Campus> campus;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
