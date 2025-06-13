package com.foryourlife.admin.sales.payments.cashDrawer.application;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CashDrawerCommandService {
    private final CashDrawerRepository repository;

    public CashDrawerCommandService(CashDrawerRepository repository) {
        this.repository = repository;
    }

    public void save(CashDrawer cashDrawer) {
        if (repository.findByNumber(cashDrawer.getNumber())) {
            throw new BaseException("Cash drawer with number " + cashDrawer.getNumber() + " already exists", List.of(""));
        }
        repository.save(cashDrawer);
    }

    public void closeDrawer(String id) {
        var cashDrawer = repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );
        cashDrawer.setOpen(false);
        repository.save(cashDrawer);
    }

    public void openDrawer(String id) {
        var cashDrawer = repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );
        if (repository.getByIsOpenAndByUserId(cashDrawer.getOpenedByUser().getId())) {
            throw new BaseException("El usuario ya tiene una caja abierta", List.of(""));
        }
        if (cashDrawer.getClosedByUser() == null) {
            throw new BaseException("La caja ya fue cerrada", List.of(""));
        }
        cashDrawer.setOpen(true);
        repository.save(cashDrawer);
    }

    public void addPaymentHistoryInCashDrawer(String paymentHistoryId, String cashDrawerId, String paymentId) {
        var cashDrawer = repository.getById(cashDrawerId).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );

        var detail = new CashDrawerDetail(UUID.randomUUID().toString(), paymentId, paymentHistoryId);

        if (cashDrawer.getOpen()) {
            throw new BaseException("La caja está abierta", List.of(""));
        }

        if (cashDrawer.getCashDrawerDetail() == null) {
            cashDrawer.setCashDrawerDetail(new ArrayList<>());
        }

        cashDrawer.getCashDrawerDetail().add(detail);
        repository.save(cashDrawer);
    }
}
