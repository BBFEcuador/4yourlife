package com.foryourlife.payments.tenant.infraestructure;

import com.foryourlife.payments.tenant.domain.Tenant;
import com.foryourlife.payments.tenant.domain.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantRepositoryImpl implements TenantRepository {
    private final JPATenantRepository repository;

    public TenantRepositoryImpl(JPATenantRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Tenant tenant) {
        this.repository.save(tenant);
    }

    @Override
    public void saveAll(List<Tenant> tenants) {
        this.repository.saveAll(tenants);
    }

    @Override
    public List<Tenant> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Optional<Tenant> findById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<Tenant> findByOwnerId(String ownerId) {
        return this.repository.findByOwnerId(ownerId);
    }
}
