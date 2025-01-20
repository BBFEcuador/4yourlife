package com.foryourlife.payments.payment.infraestructure;

import com.foryourlife.payments.payment.domain.Payment;
import com.foryourlife.payments.payment.domain.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PaymentRepositoryImpl implements PaymentRepository {
    private final JPAPaymentRepository repository;

    public PaymentRepositoryImpl(JPAPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Payment payment) {
        this.repository.save(payment);
    }

    @Override
    public void saveAll(List<Payment> payments) {
        this.repository.saveAll(payments);
    }

    @Override
    public List<Payment> getAll() {
        return this.repository.findAll();
    }

    @Override
    public List<Payment> getAllByEmail(String email) {
        return this.repository.findByEmail(email).orElseThrow();
    }
}
