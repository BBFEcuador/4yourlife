package com.foryourlife.admin.sales.payments.cashDrawer.application;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashDrawerQueryService {
    private final CashDrawerRepository repository;

    public CashDrawerQueryService(CashDrawerRepository repository) {
        this.repository = repository;
    }

    public List<CashDrawer> getAllCashDrawers() {
        return repository.getAll();
    }

    public CashDrawer getCashDrawerById(String id) {
        return repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of("The cash drawer with id " + id + " does not exist"))
        );
    }

    public List<CashDrawer> getCashDrawersByOpenedByUserId(String userId) {
        return repository.getByOpenedByUserId(userId);
    }

    public List<CashDrawer> getCashDrawersByClosedByUserId(String userId) {
        return repository.getByClosedByUserId(userId);
    }
}
