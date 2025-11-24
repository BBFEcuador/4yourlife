package com.foryourlife.admin.permission.application;

import com.foryourlife.admin.permission.domain.Permission;
import com.foryourlife.admin.permission.domain.PermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissionCommandService {
    private final PermissionRepository permissionRepository;

    public PermissionCommandService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void createPermission(String id, String name) {
        var permission = new Permission(id, name, "", true);

        permissionRepository.save(permission);
    }
}
