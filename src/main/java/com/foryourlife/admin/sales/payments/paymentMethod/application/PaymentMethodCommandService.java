package com.foryourlife.admin.sales.payments.paymentMethod.application;

import com.foryourlife.admin.bank.application.BankQueryService;
import com.foryourlife.admin.programs.campus.domain.CampusRepository;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethodRepository;
import com.foryourlife.admin.sales.payments.paymentMethod.infrastructure.httpControllers.PaymentMethodRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
        if (Objects.equals(command.code, "TRA") && command.bankId == null) {
            throw new BaseException("Para transferencias es necesario indicar el banco", List.of(""));
        }
        var campus = campusRepository.findById(command.campusId)
                .orElseThrow(() -> new BaseException("Campus no encontrado", List.of("")));

        String paymentMethodId = command.id != null ? command.id : UUID.randomUUID().toString();

        PaymentMethod paymentMethod = PaymentMethod.create(
                paymentMethodId,
                command.type,
                command.isActive,
                command.code,
                campus,
                command.bankId != null ? bankQueryService.findById(command.bankId).orElse(null) : null
        );
        repository.save(paymentMethod);
    }

    public void changeStatus(String id) {
        var pm = repository.findById(id).orElseThrow(() -> new BaseException("Campus no encontrado", List.of("No se encontro")));
        pm.setActive(!pm.getActive());
        repository.save(pm);
    }
}
