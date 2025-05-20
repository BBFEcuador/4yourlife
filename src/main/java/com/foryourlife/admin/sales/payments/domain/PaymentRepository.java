package com.foryourlife.admin.sales.payments.domain;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentRepository {
    void save(Payment payment);
    Payment findById(String id);
    List<Payment> findByParticipantId(String id, Pageable pageable);
    List<Payment> findAll(Pageable pageable);
}
