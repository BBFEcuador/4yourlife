package com.foryourlife.admin.sales.payments.cashBox.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JPAImplCashBoxRepository extends JpaRepository<CashBox, String> {
    Optional<CashBox> findByNumber(String number);
    List<CashBox> findAllByStore_Campus_Id(String campusId);

    Optional<CashBox> findByStore_IdAndNumber(String storeId, String number);

    List<CashBox> findAllByStore_Id(String storeId);
}
