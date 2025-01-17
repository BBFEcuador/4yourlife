package com.foryourlife.account.user.infrastructure.httpControllers;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserLoginRequest {
    @NotNull
    @Email
    @NotBlank(message = "The User/Email field is required")
    public String username;
    @NotNull
    @NotBlank(message = "The password field is required")
    public String password;

    public UserLoginRequest() {
    }

    public UserLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
