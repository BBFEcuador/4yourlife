package com.foryourlife.admin.sales.payments.cashBox.domain;

import java.util.List;
import java.util.Optional;

public interface CashBoxRepository {
    CashBox save(CashBox cashBox);
    Optional<CashBox> findById(String id);
    Optional<CashBox> findByNumber(String number);
    List<CashBox> findAll();
}
