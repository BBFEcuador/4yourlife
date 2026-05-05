package com.foryourlife.admin.sales.payments.cashDrawer.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

public interface CashDrawerRepository {
    CashDrawer save(CashDrawer cashDrawer);
    Optional<CashDrawer> getById(String id);
    void deleteById(String id);
    Page<CashDrawer> getAll(Criteria criteria, Pageable pageable);
    Page<CashDrawer> getByCashBoxId(Pageable pageable, String id);
    Optional<CashDrawer> findByCashBoxIdAndStatus(String cashBoxId, CashDrawerStatus status);
    Optional<CashDrawer> findByCashBoxIdAndStatusAndUserId(String cashBoxId, CashDrawerStatus status, String userId);
    String generatePdfReport(CashDrawer cashDrawer);
    Optional<CashDrawer> findByStatusIsNotAndOpenedByUserId(CashDrawerStatus cashDrawerStatus, String userId);
    Optional<CashDrawer> findByStatusIsAndOpenedByUserId(CashDrawerStatus cashDrawerStatus, String userId);
    ByteArrayOutputStream generateExcelReport(CashDrawer cashDrawer);
}
