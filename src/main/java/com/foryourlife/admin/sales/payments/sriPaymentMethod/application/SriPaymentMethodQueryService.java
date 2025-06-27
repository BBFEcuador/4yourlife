package com.foryourlife.admin.sales.payments.sriPaymentMethod.application;

import com.foryourlife.admin.sales.payments.sriPaymentMethod.domain.SriPaymentMethod;
import com.foryourlife.admin.sales.payments.sriPaymentMethod.domain.SriPaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SriPaymentMethodQueryService {
    private final SriPaymentMethodRepository repository;

    public SriPaymentMethodQueryService(SriPaymentMethodRepository repository) {
        this.repository = repository;
    }

    public List<SriPaymentMethod> findAll() {
        return repository.findAll();
    }
}
