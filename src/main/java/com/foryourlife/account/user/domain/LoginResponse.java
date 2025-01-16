package com.foryourlife.account.user.domain;

public class LoginResponse {
    public String token;

    public Users user;

    public LoginResponse(String token, Users user) {
        this.token = token;
        this.user = user;
    }
}
