package com.foryourlife.account.role.domain;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    void saveAll(List<Role> roles);
    List<Role> getAll();
    List<Role> findByCriteria(Specification<Role> specification);
    Optional<Role> findOneByCriteria(Specification<Role> specification);
    Optional<Role> findById(String id);
}
