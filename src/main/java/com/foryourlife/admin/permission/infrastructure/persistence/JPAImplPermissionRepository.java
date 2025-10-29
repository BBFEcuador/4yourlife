package com.foryourlife.admin.permission.infrastructure.persistence;

import com.foryourlife.admin.permission.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAImplPermissionRepository extends JpaRepository<Permission,String> {
}
