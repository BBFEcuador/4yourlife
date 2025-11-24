package com.foryourlife.admin.permission.domain;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository {
    void save(Permission permission);
    Optional<Permission> findById(String id);
    List<Permission> findAll();
}
