package com.foryourlife.clients.account.invoiceData.infrastructure.persistence;

import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JPAImplInvoiceData extends JpaRepository<DataInvoice, String> {
    List<DataInvoice> findAllByUser_Id(String userId);
    Optional<DataInvoice> findByDocumentAndUser_Id(String document, String userId);
}
