package com.foryourlife.account.permission.infrastructure;

import com.foryourlife.account.permission.domain.PermissionRepository;
import com.foryourlife.account.permission.domain.Permissions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionRepositoryImpl implements PermissionRepository {

    private final JPAPermissionRepository repository;

    public PermissionRepositoryImpl(JPAPermissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Permissions permissions) {
        this.repository.save(permissions);
    }

    @Override
    public List<Permissions> getAll() {
        return this.repository.findAll();
    }
}
