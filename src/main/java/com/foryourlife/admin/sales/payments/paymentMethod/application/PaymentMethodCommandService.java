package com.foryourlife.admin.sales.payments.paymentMethod.application;

import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethodRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodCommandService {
    private PaymentMethodRepository repository;

    public PaymentMethodCommandService(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    public void createPaymentMethod(PaymentMethod command) {
        repository.save(command);
    }
}
