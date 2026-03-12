package com.foryourlife.admin.sales.payments.store.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StoreRepository {
    Store save(Store store);
    Optional<Store> findById(String id);
    void deleteById(String id);
    List<Store> getByCampusId(String campusId);
    Page<Store> getAll(Pageable pageable, Criteria criteria);
    Optional<Store> findByEstablishment(String campusId, String name);
}
