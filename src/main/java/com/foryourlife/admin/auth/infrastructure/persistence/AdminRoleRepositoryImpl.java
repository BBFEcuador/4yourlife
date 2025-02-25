package com.foryourlife.admin.auth.infrastructure.persistence;

import com.foryourlife.admin.auth.domain.AdminRole;
import com.foryourlife.admin.auth.domain.AdminRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminRoleRepositoryImpl implements AdminRoleRepository {
    private final JPAAdminRoleRepository repository;

    public AdminRoleRepositoryImpl(JPAAdminRoleRepository repository) {
        this.repository = repository;
    }

    public AdminRole findById(String id){
        return repository.findById(id).get();
    }

    @Override
    public List<AdminRole> getAll() {
        return repository.findAll();
    }

    @Override
    public void saveAll(List<AdminRole> roles) {
        repository.saveAll(roles);
    }
}
