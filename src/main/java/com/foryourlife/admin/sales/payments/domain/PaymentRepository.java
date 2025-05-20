package com.foryourlife.admin.sales.payments.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentRepository {
    void save(Payment payment);
    Payment findById(String id);
    Page<Payment> findByParticipantId(String id, Pageable pageable);
    Page<Payment> findAll(Pageable pageable);
}
