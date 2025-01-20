package com.foryourlife.account.role.infraestructure;

import com.foryourlife.account.role.domain.Role;
import com.foryourlife.account.role.domain.RoleRepository;
import org.springframework.data.jpa.domain.Specification;
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
    public void saveAll(List<Role> roles) {
        this.repository.saveAll(roles);
    }

    @Override
    public Optional<Role> findById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public List<Role> getAll() {
        return this.repository.findAll();
    }

    @Override
    public List<Role> findByCriteria(Specification<Role> specification) {
        return this.repository.findAll(specification);
    }

    @Override
    public Optional<Role> findOneByCriteria(Specification<Role> specification) {
        return this.repository.findOne(specification);
    }
}
