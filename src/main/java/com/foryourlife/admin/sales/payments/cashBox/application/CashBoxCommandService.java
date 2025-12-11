package com.foryourlife.admin.sales.payments.cashBox.application;

import com.foryourlife.admin.sales.payments.cashBox.domain.CashBox;
import com.foryourlife.admin.sales.payments.cashBox.domain.CashBoxRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashBoxCommandService {
    private final CashBoxRepository repository;

    public CashBoxCommandService(CashBoxRepository repository) {
        this.repository = repository;
    }

    public void addCashBox(CashBox cashBox) {

        cashBox.setNumber(String.format("%03d", Integer.parseInt(cashBox.getNumber())));

        if (repository.findByNumberAndStoreId(cashBox.getNumber(), cashBox.getStore().getId()).isPresent())
        {
            throw new BaseException(
                    "Punto de emision ya existente",
                    List.of("El punto de emision con el numero ${request.number} ya existe!")
            );
        }

        repository.save(cashBox);
    }
}
