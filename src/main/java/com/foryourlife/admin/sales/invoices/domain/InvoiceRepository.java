package com.foryourlife.admin.sales.invoices.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvoiceRepository {
    void save(Invoice invoice);
    Invoice findById(String id);
    Page<Invoice> findByUserId(String id, Pageable pageable);
    Page<Invoice> findAll(Pageable pageable);
}
