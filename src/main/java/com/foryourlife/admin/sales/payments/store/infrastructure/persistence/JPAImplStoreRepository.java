package com.foryourlife.admin.sales.payments.store.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPAImplStoreRepository extends JpaRepository<Store, String> {
    List<Store> findAllByCampus_Id(String campusId);
}
