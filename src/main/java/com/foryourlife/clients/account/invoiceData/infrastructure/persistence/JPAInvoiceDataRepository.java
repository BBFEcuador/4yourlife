package com.foryourlife.clients.account.invoiceData.infrastructure.persistence;

import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import com.foryourlife.clients.account.invoiceData.domain.InvoiceDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPAInvoiceDataRepository implements InvoiceDataRepository {
    private final JPAImplInvoiceData implInvoiceData;

    public JPAInvoiceDataRepository(JPAImplInvoiceData implInvoiceData) {
        this.implInvoiceData = implInvoiceData;
    }

    @Override
    public void save(DataInvoice dataInvoice) {
        implInvoiceData.save(dataInvoice);
    }

    @Override
    public Optional<DataInvoice> findById(String id) {
        return implInvoiceData.findById(id);
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<DataInvoice> findAllByUserId(String userId) {
        return implInvoiceData.findAllByUser_Id(userId);
    }

    @Override
    public Optional<DataInvoice> findByDocument(String document, String userId) {
        return implInvoiceData.findByDocumentAndUser_Id(document, userId);
    }
}
