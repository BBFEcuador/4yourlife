package com.foryourlife.admin.sales.payments.payment.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepository {
    Payment save(Payment payment);
    Payment findById(String id);
    Page<Payment> findByParticipantId(String id, Pageable pageable);
    Page<Payment> findAll(Pageable pageable);
    Page<Payment> findAll(Pageable pageable, Criteria criteria);
    String generatePdf(Payment payment);
    boolean existsByParticipantIdAndStatus(String participantId, PaymentStatus status);
}
