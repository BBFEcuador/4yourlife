package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import java.util.List;

public interface CashDrawerRepository {
    void save(CashDrawer cashDrawer);
    CashDrawer getById(String id);
    void deleteById(String id);
    List<CashDrawer> getAll();
    List<CashDrawer> getByOpenedByUserId(String userId);
    List<CashDrawer> getByClosedByUserId(String userId);
}
