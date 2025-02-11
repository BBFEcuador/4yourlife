package com.foryourlife.clients.account.module.infrastructure.persistence;

import com.foryourlife.clients.account.module.domain.ClientModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAModuleRepository extends JpaRepository<ClientModule, String> {
    Optional<ClientModule> findByUser_Id(String user);
}
