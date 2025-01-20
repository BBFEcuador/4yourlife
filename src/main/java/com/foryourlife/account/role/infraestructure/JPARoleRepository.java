package com.foryourlife.account.role.infraestructure;

import com.foryourlife.account.role.domain.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPARoleRepository extends JpaRepository<Role,String>, JpaSpecificationExecutor<Role> {
    Optional<Role> findById(String id);
    List<Role> findAll(Specification<Role> specification);
    Optional<Role> findByRoleName(String roleName);
}
