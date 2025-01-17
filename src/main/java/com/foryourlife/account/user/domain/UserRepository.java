package com.foryourlife.account.user.domain;

import com.foryourlife.shared.domain.exception.BaseException;

import java.util.Optional;

public interface UserRepository {
    LoginResponse login(String username,String password) throws BaseException;
    Optional<Users> findByEmail(String email);
    Optional<Users> findById(String id);
    void save(Users user);
}
