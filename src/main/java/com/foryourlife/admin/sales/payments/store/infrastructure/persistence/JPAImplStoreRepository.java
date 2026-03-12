package com.foryourlife.admin.sales.payments.store.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface JPAImplStoreRepository extends JpaRepository<Store, String>, JpaSpecificationExecutor<Store> {
    List<Store> findAllByCampus_Id(String campusId);

    Optional<Store> findByNumber(String number);

    Optional<Store> findByCampus_IdAndNumber(String campusId, String number);
}
