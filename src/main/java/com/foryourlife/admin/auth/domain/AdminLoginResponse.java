package com.foryourlife.admin.auth.domain;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;

import java.util.List;

public class AdminLoginResponse {
    public Admin admin;

    public String token;

    public List<CashDrawer> cashDrawer;

    public AdminLoginResponse(Admin admin, String token, List<CashDrawer> cashDrawer) {
        this.admin = admin;
        this.cashDrawer = cashDrawer;
        this.token = token;
    }
}
