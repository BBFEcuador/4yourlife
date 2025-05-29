package com.foryourlife.clients.account.participant.infrastructure.httpControllers;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ParticipantLoginRequest {
    @NotNull
    @Email
    @NotBlank(message = "The User/Email field is required")
    public String username;
    @NotNull
    @NotBlank(message = "The password field is required")
    public String password;

    public ParticipantLoginRequest() {
    }

    public ParticipantLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username.toLowerCase();
    }

    public String getPassword() {
        return password;
    }
}
