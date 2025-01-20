package com.foryourlife.payments.plan.infraestructure;

import com.foryourlife.payments.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JPAPlanRepository extends JpaRepository<Plan, String> {
    Optional<Plan> findById(String id);
}
