package com.foryourlife.admin.programs.trainer.infrastructure.httpControllers;

import jakarta.validation.constraints.NotNull;

public class LoginTrainerRequest {
    @NotNull(message = "Email is required")
    public String email;
    @NotNull(message = "Password is required")
    public String password;

    public LoginTrainerRequest() {
    }

    public LoginTrainerRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
