package com.foryourlife.admin.sales.payments.paymentMethod.application;

import com.foryourlife.admin.bank.application.BankQueryService;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethodRepository;
import com.foryourlife.admin.sales.payments.paymentMethod.infrastructure.httpControllers.PaymentMethodRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodCommandService {
    private PaymentMethodRepository repository;
    private CampusRepository campusRepository;
    private BankQueryService bankQueryService;

    public PaymentMethodCommandService(PaymentMethodRepository repository, CampusRepository campusRepository) {
        this.repository = repository;
        this.campusRepository = campusRepository;
    }

    public void createPaymentMethod(PaymentMethodRequest command) {
        var campus = campusRepository.findById(command.campusId).orElseThrow(() -> new BaseException("Campus no encontrado", List.of("")));
        var bank = bankQueryService.findById(command.bankId).orElse(null);
        repository.save(new PaymentMethod(
                command.id,
                command.type,
                command.isActive,
                command.code,
                campus,
                bank
        ));
    }
}
