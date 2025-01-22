package com.foryourlife.admin.auth.domain;

public class AdminLoginResponse {
    private Admin admin;

    private String token;

    public AdminLoginResponse(Admin admin, String token) {
        this.admin = admin;
        this.token = token;
    }
}
