package com.foryourlife.admin.auth.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {
    AdminLoginResponse login(String username, String password);
    Optional<Admin> findById(String id);
    Optional<Admin> findByEmail(String email);
    Page<Admin> findAll(Pageable pageable, Criteria criteria);
    Page<Admin> findAll(Pageable pageable);
    Admin save(Admin admin);
    void deleteById(String id);
    Optional<Admin> findByUserId(String userId);
}
