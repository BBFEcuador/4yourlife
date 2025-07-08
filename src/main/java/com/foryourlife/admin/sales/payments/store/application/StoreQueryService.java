package com.foryourlife.admin.sales.payments.store.application;

import com.foryourlife.admin.sales.payments.store.domain.Store;
import com.foryourlife.admin.sales.payments.store.domain.StoreRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreQueryService {
    private final StoreRepository repository;

    public StoreQueryService(StoreRepository repository) {
        this.repository = repository;
    }

    public Store getStoreById(String id) {
        return repository.findById(id).orElseThrow(
                ()-> new BaseException("Store not found", List.of("The store does not exist"))
        );
    }

    public List<Store> getAllStoresByCampus(String campusId) {
        return repository.getByCampusId(campusId);
    }

    public List<Store> getAllStores(){
        return repository.getAll();
    }
}
