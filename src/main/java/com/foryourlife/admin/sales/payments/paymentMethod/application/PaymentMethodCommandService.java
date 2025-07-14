package com.foryourlife.admin.sales.payments.paymentMethod.application;

import com.foryourlife.admin.bank.application.BankQueryService;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethodRepository;
import com.foryourlife.admin.sales.payments.paymentMethod.infrastructure.httpControllers.PaymentMethodRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentMethodCommandService {
    private final PaymentMethodRepository repository;
    private final CampusRepository campusRepository;
    private final BankQueryService bankQueryService;

    public PaymentMethodCommandService(PaymentMethodRepository repository, CampusRepository campusRepository, BankQueryService bankQueryService) {
        this.repository = repository;
        this.campusRepository = campusRepository;
        this.bankQueryService = bankQueryService;
    }

    public void createPaymentMethod(PaymentMethodRequest command) {
        var campus = campusRepository.findById(command.campusId)
                .orElseThrow(() -> new BaseException("Campus no encontrado", List.of("")));
        var bank = bankQueryService.findById(command.bankId).orElse(null);
        
        String paymentMethodId = command.id != null ? command.id : UUID.randomUUID().toString();
        
        PaymentMethod paymentMethod = PaymentMethod.create(
                paymentMethodId,
                command.type,
                command.isActive,
                command.code,
                campus,
                bank
        );
        repository.save(paymentMethod);
    }
}
