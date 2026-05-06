package com.foryourlife.admin.auth.domain;

public class AdminLoginResponse {
    public Admin admin;

    public String token;

    public AdminLoginResponse(Admin admin, String token) {
        this.admin = admin;
        this.token = token;
    }
}
