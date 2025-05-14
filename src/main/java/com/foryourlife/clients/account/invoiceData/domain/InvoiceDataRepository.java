package com.foryourlife.clients.account.invoiceData.domain;

import java.util.List;
import java.util.Optional;

public interface InvoiceDataRepository {
    void save(DataInvoice dataInvoice);
    Optional<DataInvoice> findById(String id);
    void deleteById(String id);
    List<DataInvoice> findAllByUserId(String userId);
    Optional<DataInvoice> findByDocument(String document, String userId);
}
