package com.foryourlife.payments.payment.domain;

import java.util.List;

public interface PaymentRepository {
    void save(Payment payment);
    void saveAll(List<Payment> payments);
    List<Payment> getAll();
    List<Payment> getAllByEmail(String email);
}
