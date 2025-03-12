package com.foryourlife.admin.auth.domain;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    AdminLoginResponse login(String username, String password);
    Optional<Admin> findById(String id);
    Optional<Admin> findByEmail(String email);
    List<Admin> getAll();
    Admin save(Admin admin);
    void deleteById(String id);
    Optional<Admin> findByUserId(String userId);
}
