package com.foryourlife.payments.payment.infraestructure;

import com.foryourlife.payments.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JPAPaymentRepository extends JpaRepository<Payment, String> {
    Optional<List<Payment>> findByEmail(String email);
}
