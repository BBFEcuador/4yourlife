package com.foryourlife.admin.bank.application;

import com.foryourlife.admin.bank.domain.Bank;
import com.foryourlife.admin.bank.domain.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankQueryService {
    private final BankRepository repository;

    public BankQueryService(BankRepository repository) {
        this.repository = repository;
    }

    public List<Bank> findAllBanks() {
        return repository.getAll();
    }

    public Bank findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Bank findByCampusId(String CampusId) {
        return repository.findByCampusId(CampusId).orElse(null);
    }
}
