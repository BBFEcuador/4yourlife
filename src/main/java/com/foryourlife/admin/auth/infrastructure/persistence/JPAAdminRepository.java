package com.foryourlife.admin.auth.infrastructure.persistence;

import com.foryourlife.admin.auth.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAAdminRepository extends JpaRepository<Admin, String>, JpaSpecificationExecutor<Admin> {
    Optional<Admin> findByUser_email(String email);
    Optional<Admin> findByUser_id(String id);
}
