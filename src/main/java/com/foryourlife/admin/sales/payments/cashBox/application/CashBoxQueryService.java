package com.foryourlife.admin.sales.payments.cashBox.application;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBoxRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashBoxQueryService {
    private final CashBoxRepository repository;

    public CashBoxQueryService(CashBoxRepository repository) {
        this.repository = repository;
    }

    public List<CashBox> getAllCashBox() {
        return repository.findAll();
    }

    public List<CashBox> getAllCashBoxByCampus(String campusId) {
        return repository.findAllByCampus(campusId);
    }

    public CashBox getCashBoxById(String id) {
        return repository.findById(id).orElseThrow(() -> new BaseException("Cashbox not found", List.of("The cashbox with id " + id + " does not exist")));
    }

    public List<CashBox> getAllCashBoxByStoreId(String storeId) {
        return repository.findAllByStoreId(storeId);
    }
}
