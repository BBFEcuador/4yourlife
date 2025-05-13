package com.foryourlife.clients.account.invoiceData.application;

import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import com.foryourlife.clients.account.invoiceData.domain.InvoiceDataRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceDataQueryService {
    private final InvoiceDataRepository repository;

    public InvoiceDataQueryService(InvoiceDataRepository repository) {
        this.repository = repository;
    }

    public DataInvoice findById(String id) {
        return repository.findById(id).orElseThrow(() -> new BaseException("Invoice not found", List.of("The invoice with id " + id + " does not exist")));
    }

    public List<DataInvoice> getAllInvoiceDataByUserId(String userId) {
        return repository.findAllByUserId(userId);
    }
}
