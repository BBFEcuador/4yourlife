package com.foryourlife.admin.sales.payments.cashBox.application;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBoxRepository;
import org.springframework.stereotype.Service;

@Service
public class CashBoxCommandService {
    private final CashBoxRepository repository;

    public CashBoxCommandService(CashBoxRepository repository) {
        this.repository = repository;
    }

    public CashBox addCashBox(CashBox CashBox) {
        return repository.save(CashBox);
    }
}
