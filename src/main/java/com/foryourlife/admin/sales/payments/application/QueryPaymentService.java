package com.foryourlife.admin.sales.payments.application;

import com.foryourlife.admin.sales.payments.domain.Payment;
import com.foryourlife.admin.sales.payments.domain.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QueryPaymentService {
    private final PaymentRepository _paymentRepository;

    public QueryPaymentService(PaymentRepository paymentRepository) {
        _paymentRepository = paymentRepository;
    }

    public Payment findById(String id){
        return _paymentRepository.findById(id);
    }

    public Page<Payment> findAll(Pageable pageable){
        return _paymentRepository.findAll(pageable);
    }

    public Page<Payment> findByParticipantId(String id, Pageable pageable){
        return  _paymentRepository.findByParticipantId(id, pageable);
    }
}
