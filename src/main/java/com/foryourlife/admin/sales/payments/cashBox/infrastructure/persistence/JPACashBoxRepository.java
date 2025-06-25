package com.foryourlife.admin.sales.payments.cashBox.infrastructure.persistence;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBoxRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPACashBoxRepository implements CashBoxRepository {
    private final JPAImplCashBoxRepository repository;

    public JPACashBoxRepository(JPAImplCashBoxRepository repository) {
        this.repository = repository;
    }

    @Override
    public CashBox save(CashBox cashBox) {
        return repository.save(cashBox);
    }

    @Override
    public Optional<CashBox> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<CashBox> findByNumber(String number) {
        return repository.findByNumber(number);
    }

    @Override
    public List<CashBox> findAll() {
        return repository.findAll();
    }

}
