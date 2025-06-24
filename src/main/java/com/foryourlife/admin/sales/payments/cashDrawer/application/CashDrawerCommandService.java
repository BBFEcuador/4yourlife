package com.foryourlife.admin.sales.payments.cashDrawer.application;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CashDrawerCommandService {
    private final CashDrawerRepository repository;
    private final UserRepository userRepository;

    public CashDrawerCommandService(CashDrawerRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void save(CashDrawer cashDrawer) {
        if (repository.findByNumber(cashDrawer.getNumber())) {
            throw new BaseException("Cash drawer with number " + cashDrawer.getNumber() + " already exists", List.of(""));
        }
        repository.save(cashDrawer);
    }

    public void closeDrawer(String id, String userId) {
        var cashDrawer = repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );
        cashDrawer.setStatus(CashDrawerStatus.CLOSED);
        var user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException("User not found", List.of(""))
        );
        cashDrawer.setClosedByUser(user);
        repository.save(cashDrawer);
    }

    public void openDrawer(String id, String userId) {
        var cashDrawer = repository.getById(id).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );

        if (repository.getByIsOpenAndByUserId(userId)) {
            throw new BaseException("El usuario ya tiene una caja abierta", List.of(""));
        }
        if (cashDrawer.getStatus() == CashDrawerStatus.CLOSED) {
            throw new BaseException("La caja ya fue cerrada", List.of(""));
        }
        cashDrawer.setStatus(CashDrawerStatus.OPEN);
        var user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException("User not found", List.of(""))
        );
        cashDrawer.setClosedByUser(user);
        repository.save(cashDrawer);
    }
}
