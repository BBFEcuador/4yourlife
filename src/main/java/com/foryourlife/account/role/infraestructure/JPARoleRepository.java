package com.foryourlife.account.role.infraestructure;

import com.foryourlife.account.role.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPARoleRepository extends JpaRepository<Role,String> {
    Optional<Role> findById(String id);
    Optional<Role> findByRoleName(String roleName);
}
