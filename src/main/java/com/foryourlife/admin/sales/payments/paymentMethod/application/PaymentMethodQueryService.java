package com.foryourlife.admin.sales.payments.paymentMethod.application;

import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethodRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodQueryService {
    private PaymentMethodRepository repository;

    public PaymentMethodQueryService(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    public List<PaymentMethod> getAll(Criteria criteria) {
        return repository.getAll(criteria);
    }

    public PaymentMethod getById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new BaseException("Not found", List.of("Payment method not found"))
        );
    }
}
