package com.foryourlife.admin.sales.payments.paymentMethod.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAImplPaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
}
