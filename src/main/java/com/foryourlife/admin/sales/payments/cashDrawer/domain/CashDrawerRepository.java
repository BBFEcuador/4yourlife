package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import java.util.List;
import java.util.Optional;

public interface CashDrawerRepository {
    CashDrawer save(CashDrawer cashDrawer);
    Optional<CashDrawer> getById(String id);
    void deleteById(String id);
    List<CashDrawer> getAll();
    List<CashDrawer> getByCashBoxId(String id);
    List<CashDrawer> getByUserIdAndStatusOpenOrLock(String userId);
    Optional<CashDrawer> getByIsOpenAndByUserId(String userId);
    Optional<CashDrawer> findByCashBoxIdAndStatus(String id, CashDrawerStatus status);
    String generatePdfReport(CashDrawer cashDrawer);
}
