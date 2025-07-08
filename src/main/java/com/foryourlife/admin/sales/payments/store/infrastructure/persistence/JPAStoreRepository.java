package com.foryourlife.admin.sales.payments.store.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.store.domain.Store;
import com.foryourlife.admin.sales.payments.store.domain.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPAStoreRepository implements StoreRepository {
    private final JPAImplStoreRepository repository;

    public JPAStoreRepository(JPAImplStoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public Store save(Store store) {
        return repository.save(store);
    }

    @Override
    public Optional<Store> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Store> getByCampusId(String campusId) {
        return repository.findAllByCampus_Id(campusId);
    }

    @Override
    public Optional<Store> findByEstablishment(String campusId, String name) {
        return repository.findByCampus_IdAndNumber(campusId, name);
    }

    @Override
    public List<Store> getAll() {
        return repository.findAll();
    }
}
