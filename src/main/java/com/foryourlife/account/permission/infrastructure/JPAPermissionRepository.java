package com.foryourlife.account.permission.infrastructure;

import com.foryourlife.account.permission.domain.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAPermissionRepository extends JpaRepository<Permissions,String> {
}
