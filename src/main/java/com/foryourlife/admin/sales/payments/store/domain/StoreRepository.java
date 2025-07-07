package com.foryourlife.admin.sales.payments.store.domain;

import java.util.List;
import java.util.Optional;

public interface StoreRepository {
    void save(Store store);
    Optional<Store> findById(String id);
    void deleteById(String id);
    List<Store> getByCampusId(String campusId);
}
