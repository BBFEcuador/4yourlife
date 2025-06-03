package com.foryourlife.admin.sales.invoices.infraestructure.persistence;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPAInvoiceRepository extends JpaRepository<Invoice, String>, JpaSpecificationExecutor<Invoice> {
    Optional<Invoice> findTopBySentSriIsTrueOrderByInvoiceDateDesc();

    List<Invoice> findAllByPayment_Id(String paymentId);

    Page<Invoice> findAllByPayment_Participant_Id(String paymentParticipantId, Pageable pageable);
}
