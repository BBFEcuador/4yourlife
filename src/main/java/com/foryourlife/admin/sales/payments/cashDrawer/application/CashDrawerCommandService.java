package com.foryourlife.admin.sales.payments.cashDrawer.application;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import org.springframework.stereotype.Service;

@Service
public class CashDrawerCommandService {
    private final CashDrawerRepository repository;

    public CashDrawerCommandService(CashDrawerRepository repository) {
        this.repository = repository;
    }

    public void save(CashDrawer cashDrawer) {
        repository.save(cashDrawer);
    }
    public void closeDrawer(String id) {
        var cashDrawer = repository.getById(id);
        cashDrawer.setOpen(false);
        repository.save(cashDrawer);
    }
    public void openDrawer(String id) {
        var cashDrawer = repository.getById(id);
        cashDrawer.setOpen(true);
    }
}
