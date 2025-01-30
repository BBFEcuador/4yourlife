package com.foryourlife.admin.auth.infrastructure.persistence;

import com.foryourlife.admin.auth.domain.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAAdminRoleRepository extends JpaRepository<AdminRole, String> {
}
