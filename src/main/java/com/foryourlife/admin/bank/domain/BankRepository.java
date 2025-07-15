package com.foryourlife.admin.bank.domain;

import java.util.List;
import java.util.Optional;

public interface BankRepository {
    void saveBank(Bank bank);
    Optional<Bank> findById(String id);
    void deleteById(String id);
    List<Bank> getAll();
    List<Bank> findByCampusId(String campusId);
    Optional<Bank> findByContificoId(String contificoId);
}
