package com.foryourlife.admin.sales.invoices.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    Invoice findById(String id);
    Page<Invoice> findByUserId(String id, Pageable pageable);
    Page<Invoice> findAll(Pageable pageable, Criteria criteria);
    Optional<Invoice> findLastInvoice();
    List<Invoice> findAllInvoiceByPaymentId(String paymentId);
    List<Invoice> findInvoicesBySentContifico(Boolean sentContifico);
    List<Invoice> findInvoicesBySentContificoAndCampusId(Boolean sentContifico, String campusId);
    Optional<Invoice> findInvoiceByPaymentId(String paymentId);
}
