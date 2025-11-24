package com.foryourlife.admin.permission.application;

import com.foryourlife.admin.permission.domain.Permission;
import com.foryourlife.admin.permission.domain.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionQueryService {
    private final PermissionRepository permissionRepository;

    public PermissionQueryService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findById(String id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }
}
