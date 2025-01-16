package com.foryourlife.account.user.domain;

import java.util.Optional;

public interface UserRepository {
    LoginResponse login(String username,String password);
    Optional<Users> findByEmail(String email);
    Optional<Users> findById(String id);
    void save(Users user);
}
