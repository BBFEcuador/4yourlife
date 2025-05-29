package com.foryourlife.admin.sales.invoices.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InvoiceRepository {
    void save(Invoice invoice);
    Invoice findById(String id);
    Page<Invoice> findByUserId(String id, Pageable pageable);
    Page<Invoice> findAll(Pageable pageable);
    Optional<Invoice> findLastInvoice();
    Optional<Invoice> findInvoiceByPaymentId(String paymentId);
}
