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
    Optional<CashDrawer> findByCashBoxIdAndStatus(String cashBoxId, CashDrawerStatus status);
    Optional<CashDrawer> findByCashBoxIdAndStatusAndUserId(String cashBoxId, CashDrawerStatus status, String userId);
    String generatePdfReport(CashDrawer cashDrawer);
    Optional<CashDrawer> findByStatusAndOpenedByUserId(CashDrawerStatus cashDrawerStatus, String userId);
}
