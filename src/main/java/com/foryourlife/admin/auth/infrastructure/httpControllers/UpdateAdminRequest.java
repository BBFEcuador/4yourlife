package com.foryourlife.admin.auth.infrastructure.httpControllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UpdateAdminRequest {
    @NotNull
    public String id;
    @NotNull
    public String name;
    @NotNull
    @Email
    public String email;
    @NotNull
    public String password;

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
