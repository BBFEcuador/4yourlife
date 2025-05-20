package com.foryourlife.admin.sales.payments.infraestructure.persistence;

import com.foryourlife.admin.sales.payments.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAPaymentRepository extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {
    Page<Payment> findByParticipantId(String id, Pageable page);
}
