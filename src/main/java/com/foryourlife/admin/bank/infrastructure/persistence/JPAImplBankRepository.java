package com.foryourlife.admin.bank.infrastructure.persistence;

import com.foryourlife.admin.bank.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JPAImplBankRepository extends JpaRepository<Bank, String> {
    Optional<Bank> findByCampus_Id(String campusId);

    Optional<Bank> findByContificoId(String contificoId);
}
