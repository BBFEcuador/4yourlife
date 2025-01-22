package com.foryourlife.admin.auth.infrastructure.persistence;

import com.foryourlife.admin.auth.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAImplAdminRepository extends JpaRepository<Admin, String>, JpaSpecificationExecutor<Admin> {
    Optional<Admin> findByEmail(String email);
}
