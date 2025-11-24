package com.foryourlife.admin.permission.infrastructure.persistence;

import com.foryourlife.admin.permission.domain.Permission;
import com.foryourlife.admin.permission.domain.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPAPermissionRepository implements PermissionRepository {
    private final JPAImplPermissionRepository repository;

    public JPAPermissionRepository(JPAImplPermissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Permission permission) {
        repository.save(permission);
    }

    @Override
    public Optional<Permission> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Permission> findAll() {
        return repository.findAll();
    }
}
