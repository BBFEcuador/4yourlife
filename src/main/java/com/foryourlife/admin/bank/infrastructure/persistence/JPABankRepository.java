package com.foryourlife.admin.bank.infrastructure.persistence;

import com.foryourlife.admin.bank.domain.Bank;
import com.foryourlife.admin.bank.domain.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPABankRepository implements BankRepository {
    private final JPAImplBankRepository repository;

    public JPABankRepository(JPAImplBankRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveBank(Bank bank) {
        repository.save(bank);
    }

    @Override
    public Optional<Bank> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Bank> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Bank> findByCampusId(String campusId) {
        return repository.findByCampus_Id(campusId);
    }

    @Override
    public Optional<Bank> findByContificoId(String contificoId) {
        return repository.findByContificoId(contificoId);
    }
}
