package com.foryourlife.admin.sales.payments.sriPaymentMethod.infrastructure;

import com.foryourlife.admin.sales.payments.sriPaymentMethod.domain.SriPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAImplSriPaymentMethodRepository extends JpaRepository<SriPaymentMethod, String> {
}
