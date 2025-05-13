package com.foryourlife.clients.account.invoiceData.application;

import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import com.foryourlife.clients.account.invoiceData.domain.InvoiceDataRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceDataCommandService {
    private final InvoiceDataRepository repository;

    public InvoiceDataCommandService(InvoiceDataRepository repository) {
        this.repository = repository;
    }

    public void create(DataInvoice dataInvoice) {
        repository.findByDocument(dataInvoice.getUser().getId(), dataInvoice.getDocument()).ifPresent(existingInvoice -> {
            throw new BaseException("Invoice already exists for this user", List.of(""));
        });
        repository.save(dataInvoice);
    }
}
