package com.foryourlife.admin.sales.payments.infraestructure.persistence;

import com.foryourlife.admin.sales.payments.domain.Payment;
import com.foryourlife.admin.sales.payments.domain.PaymentRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JPAPaymentRepository _jpaPaymentRepository;

    public PaymentRepositoryImpl(JPAPaymentRepository jpaPaymentRepository) {
        _jpaPaymentRepository = jpaPaymentRepository;
    }

    @Override
    public void save(Payment payment) {
        _jpaPaymentRepository.save(payment);
    }

    @Override
    public Payment findById(String id) {
        return _jpaPaymentRepository.findById(id).orElseThrow(()-> new BaseException("Payment not found", List.of("")));
    }

    @Override
    public Page<Payment> findByParticipantId(String id, Pageable pageable) {
        return _jpaPaymentRepository.findByParticipantId(id, pageable);
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return _jpaPaymentRepository.findAll(pageable);
    }
}
