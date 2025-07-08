package com.foryourlife.admin.sales.payments.paymentMethod.domain;

import com.foryourlife.shared.domain.criteria.Criteria;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository {
     void save(PaymentMethod paymentMethod);
     Optional<PaymentMethod> findById(String id);
     void changeStatus(String id, Boolean isActive);
     List<PaymentMethod> getAll(Criteria criteria);
    Boolean exist(String id);
}
