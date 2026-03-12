package com.foryourlife.admin.sales.payments.cashBox.domain;

import java.util.List;
import java.util.Optional;

public interface CashBoxRepository {
    CashBox save(CashBox cashBox);
    Optional<CashBox> findById(String id);
    Optional<CashBox> findByNumberAndStoreId(String number,  String storeId);
    List<CashBox> findAll();
    List<CashBox> findAllByCampus(String campusId);
    List<CashBox> findAllByStoreId(String storeId);
}
