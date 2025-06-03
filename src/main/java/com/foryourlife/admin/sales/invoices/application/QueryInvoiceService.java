package com.foryourlife.admin.sales.invoices.application;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QueryInvoiceService {
    private final InvoiceRepository invoiceRepository;

    public QueryInvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice findById(String id) {
        return invoiceRepository.findById(id);
    }

    public Page<Invoice> findAll(Pageable pageable) {
        return invoiceRepository.findAll(pageable);
    }

    public Page<Invoice> findByUserId(String id, Pageable pageable) {
        return invoiceRepository.findByUserId(id, pageable);
    }

    public Invoice findLastInvoice() {
        return invoiceRepository.findLastInvoice().orElseThrow(() -> new BaseException("Not found", List.of("Last invoice not found")));
    }

    public List<Invoice> findByPaymentId(String paymentId) {
        return invoiceRepository.findAllInvoiceByPaymentId(paymentId);

    }
}
