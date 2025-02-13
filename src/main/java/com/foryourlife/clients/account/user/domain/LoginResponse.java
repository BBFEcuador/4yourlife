package com.foryourlife.clients.account.user.domain;

public class LoginResponse {
    public String token;

    public Participant user;

    public LoginResponse(String token, Participant user) {
        this.token = token;
        this.user = user;
    }
}
