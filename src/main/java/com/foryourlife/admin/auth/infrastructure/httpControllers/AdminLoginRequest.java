package com.foryourlife.admin.auth.infrastructure.httpControllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AdminLoginRequest {
    @NotBlank(message = "username cannot be blank")
    @Email(message = "Not valid email")
    public String username;
    @NotBlank(message = "Password cannot be blank")
    public String password;

    public AdminLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AdminLoginRequest() {
    }

    public String getEmail() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
