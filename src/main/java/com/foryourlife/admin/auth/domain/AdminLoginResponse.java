package com.foryourlife.admin.auth.domain;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;

import java.util.List;

public class AdminLoginResponse {
    public Admin admin;

    public String token;

    public AdminLoginResponse(Admin admin, String token) {
        this.admin = admin;
        this.token = token;
    }
}
