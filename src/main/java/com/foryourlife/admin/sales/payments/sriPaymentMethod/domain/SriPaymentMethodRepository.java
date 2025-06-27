package com.foryourlife.admin.sales.payments.sriPaymentMethod.domain;

import java.util.List;

public interface SriPaymentMethodRepository {
    void save(SriPaymentMethod sriPaymentMethod);
    List<SriPaymentMethod> findAll();
}
