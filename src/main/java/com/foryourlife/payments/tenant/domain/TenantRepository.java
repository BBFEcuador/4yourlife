package com.foryourlife.payments.tenant.domain;

import java.util.List;
import java.util.Optional;

public interface TenantRepository {
    void save(Tenant tenant);

    void saveAll(List<Tenant> tenants);

    List<Tenant> getAll();

    Optional<Tenant> findById(String id);

    Optional<Tenant> findByOwnerId (String ownerId);
}
