package com.foryourlife.admin.bank.application;

import com.foryourlife.admin.bank.domain.Bank;
import com.foryourlife.admin.bank.domain.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankQueryService {
    private final BankRepository repository;

    public BankQueryService(BankRepository repository) {
        this.repository = repository;
    }

    public List<Bank> findAllBanks() {
        return repository.getAll();
    }

    public Optional<Bank> findById(String id) {
        return repository.findById(id);
    }

    public List<Bank> findByCampusId(String CampusId) {
        return repository.findByCampusId(CampusId);
    }
}
