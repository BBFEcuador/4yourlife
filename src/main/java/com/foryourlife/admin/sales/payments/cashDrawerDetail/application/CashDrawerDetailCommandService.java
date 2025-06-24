package com.foryourlife.admin.sales.payments.cashDrawerDetail.application;

import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerStatus;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.domain.CashDrawerDetailRepository;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CashDrawerDetailCommandService {
    private final CashDrawerRepository cashDrawerRepository;
    private final CashDrawerDetailRepository repository;
    private final UserRepository userRepository;

    public CashDrawerDetailCommandService(CashDrawerRepository cashDrawerRepository, CashDrawerDetailRepository repository, UserRepository userRepository) {
        this.cashDrawerRepository = cashDrawerRepository;
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void save(String paymentHistoryId, String cashDrawerId, Payment payment, String userId) {
        var cashDrawer = cashDrawerRepository.getById(cashDrawerId).orElseThrow(
                () -> new BaseException("Cash drawer not found", List.of(""))
        );

        if (cashDrawer.getStatus() != CashDrawerStatus.OPEN) {
            throw new BaseException("La caja no está abierta", List.of(""));
        }

        var user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException("User not found", List.of(""))
        );
        var detail = new CashDrawerDetail(UUID.randomUUID().toString(), payment, paymentHistoryId,cashDrawer,user);

        repository.save(detail);
    }
    public void deleteByPaymentHistoryId(String paymentHistoryId) {
        repository.deleteByPaymentHistoryId(paymentHistoryId);
    }
}
