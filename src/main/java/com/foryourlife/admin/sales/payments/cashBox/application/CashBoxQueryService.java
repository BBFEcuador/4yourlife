package com.foryourlife.admin.sales.payments.cashBox.application;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBoxRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CashBoxQueryService {
    private final CashBoxRepository repository;

    public CashBoxQueryService(CashBoxRepository repository) {
        this.repository = repository;
    }

    public List<CashBox> getAllCashBox() {
        return repository.findAll();
    }

    public CashBox getCashBoxById(String id) {
        return repository.findById(id).orElseThrow(() -> new BaseException("Cashbox not found", List.of("The cashbox with id " + id + " does not exist")));
    }

    public CashBox getCashBoxByNumber(String number) {
        return repository.findByNumber(number).orElseThrow(() -> new BaseException("Cashbox not found", List.of("The cashbox with number " + number + " does not exist")));
    }

    public List<CashBox> getCashBoxNotOpened(String campusId) {
        return repository.findAll()
                .stream()
                .filter(cashBox ->
                        (cashBox.cashDrawer.isEmpty() || cashBox.cashDrawer.stream().noneMatch(cashDrawer -> cashDrawer.getStatus() == CashDrawerStatus.OPEN)) && cashBox.getStore().getCampus().getId().equals(campusId))
                .collect(Collectors.toList());
    }
}
