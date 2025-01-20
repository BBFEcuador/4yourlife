package com.foryourlife.payments.tenant.infraestructure;

import com.foryourlife.payments.tenant.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JPATenantRepository extends JpaRepository<Tenant, String> {
    Optional<Tenant> findById(String id);

    Optional<Tenant> findByOwnerId(String ownerId);
}
