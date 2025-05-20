package com.foryourlife.admin.sales.invoices.infraestructure.persistence;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final JPAInvoiceRepository jpaInvoiceRepository;

    public InvoiceRepositoryImpl(JPAInvoiceRepository jpaInvoiceRepository) {
        this.jpaInvoiceRepository = jpaInvoiceRepository;
    }

    @Override
    public void save(Invoice invoice) {
        jpaInvoiceRepository.save(invoice);
    }

    @Override
    public Invoice findById(String id) {
        return jpaInvoiceRepository.findById(id).orElseThrow(() -> new BaseException("Invoice not found", List.of("")));
    }

    @Override
    public Page<Invoice> findByUserId(String id, Pageable pageable) {
        return jpaInvoiceRepository.findAllByDataInvoiceUserId(id,pageable);
    }

    @Override
    public Page<Invoice> findAll(Pageable pageable) {
        return jpaInvoiceRepository.findAll(pageable);
    }
}
