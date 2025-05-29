package com.foryourlife.clients.account.participant.domain;

public class LoginResponse {
    public String token;

    public Participant user;

    public LoginResponse(String token, Participant user) {
        this.token = token;
        this.user = user;
    }
}
