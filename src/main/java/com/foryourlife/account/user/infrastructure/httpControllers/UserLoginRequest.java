package com.foryourlife.account.user.infrastructure.httpControllers;

import org.jetbrains.annotations.NotNull;

public class UserLoginRequest {
    @NotNull
    public String username;
    @NotNull
    public String password;

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
