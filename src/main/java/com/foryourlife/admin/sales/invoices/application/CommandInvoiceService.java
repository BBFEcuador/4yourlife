package com.foryourlife.admin.sales.invoices.application;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceRepository;
import com.foryourlife.admin.sales.invoices.infraestructure.http.InvoiceRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommandInvoiceService {
    private final InvoiceRepository invoiceRepository;

    public CommandInvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public void update(InvoiceRequest invoiceReq) {
        if (invoiceReq.id == null) {
            throw new IllegalArgumentException("No se puede actualizar, ID nula");
        }
        if (invoiceRepository.findById(invoiceReq.id).getSentSri() == true)
            throw new IllegalArgumentException("No se puede actualizar, ya fue enviada al SRI");
        var invoice = Invoice.create(
                invoiceReq.id,
                invoiceReq.invoiceNumber,
                invoiceReq.invoiceDate,
                invoiceReq.dataInvoice,
                invoiceReq.products,
                invoiceReq.payment,
                invoiceReq.sentSri
        );
        invoiceRepository.save(invoice);
    }
}
