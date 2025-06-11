package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import java.util.List;
import java.util.Optional;

public interface CashDrawerRepository {
    void save(CashDrawer cashDrawer);
    Optional<CashDrawer> getById(String id);
    void deleteById(String id);
    List<CashDrawer> getAll();
    List<CashDrawer> getByOpenedByUserId(String userId);
    List<CashDrawer> getByClosedByUserId(String userId);
    Boolean findByNumber(String number);
    Boolean getByIsOpenAndByUserId(String userId);
}
