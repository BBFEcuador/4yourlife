package com.foryourlife.account.user.domain;

public interface UserRepository {
    LoginResponse login(String username,String password);
}
