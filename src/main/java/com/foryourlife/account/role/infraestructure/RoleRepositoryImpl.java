package com.foryourlife.account.role.infraestructure;

import com.foryourlife.account.role.domain.Role;
import com.foryourlife.account.role.domain.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleRepositoryImpl implements RoleRepository {

    private final JPARoleRepository repository;

    public RoleRepositoryImpl(JPARoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Role role) {
        this.repository.save(role);
    }

    public Optional<Role> findById(String id) {
        return this.repository.findById(id);
    }

    public Optional<Role> findByRoleName(String roleName) {
        return this.repository.findByRoleName(roleName);
    }

    @Override
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

    @Override
    public List<Role> getAll() {
        return this.repository.findAll();
    }
}
