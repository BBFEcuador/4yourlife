package com.foryourlife.admin.sales.payments.payment.application;

import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryPaymentService {
    private final PaymentRepository _paymentRepository;

    public QueryPaymentService(PaymentRepository paymentRepository) {
        _paymentRepository = paymentRepository;
    }

    public Payment findById(String id) {
        return _paymentRepository.findById(id);
    }

    public Page<Payment> findAll(Pageable pageable) {
        return _paymentRepository.findAll(pageable);
    }

    public Page<Payment> findAll(Pageable pageable, Criteria criteria) {
        return _paymentRepository.findAll(pageable, criteria);
    }

    public Page<Payment> findByParticipantId(String id, Pageable pageable) {
        return _paymentRepository.findByAllParticipantId(id, pageable);
    }

    public List<Payment> findAllByParticipantIn(String id) {
        return _paymentRepository.findByAllParticipantId(id);
    }
}
