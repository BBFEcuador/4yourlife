package com.foryourlife.admin.bank.infrastructure.persistence;

import com.foryourlife.admin.bank.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JPAImplBankRepository extends JpaRepository<Bank, String> {
    List<Bank> findByCampus_Id(String campusId);

    Optional<Bank> findByContificoId(String contificoId);
}
