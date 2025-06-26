package com.foryourlife.admin.sales.payments.sriPaymentMethod.infrastructure;

import com.foryourlife.admin.sales.payments.sriPaymentMethod.domain.SriPaymentMethod;
import com.foryourlife.admin.sales.payments.sriPaymentMethod.domain.SriPaymentMethodRepository;
import org.springframework.stereotype.Service;

@Service
public class JPASriPaymentMethodRepository implements SriPaymentMethodRepository {
    private final JPAImplSriPaymentMethodRepository repository;

    public JPASriPaymentMethodRepository(JPAImplSriPaymentMethodRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(SriPaymentMethod sriPaymentMethod) {
        repository.save(sriPaymentMethod);
    }
}
