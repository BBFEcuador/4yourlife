package com.foryourlife.admin.sales.payments.application;

import com.foryourlife.admin.sales.payments.domain.Payment;
import com.foryourlife.admin.sales.payments.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.infraestructure.httpControllers.PaymentRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommandPaymentService {

    private final PaymentRepository _paymentRepository;

    public CommandPaymentService(PaymentRepository paymentRepository) {
        _paymentRepository = paymentRepository;
    }

    public void save(PaymentRequest paymentReq){
        var payment = Payment.create(
                paymentReq.id != null ? paymentReq.id : UUID.randomUUID().toString(),
                paymentReq.product,
                paymentReq.discount,
                paymentReq.participant,
                paymentReq.campus,
                paymentReq.paymentshistory,
                paymentReq.total,
                paymentReq.status != null ? paymentReq.status : "PENDING"
        );
        _paymentRepository.save(payment);
    }

    public void update(PaymentRequest paymentReq){
        if(paymentReq.id == null || paymentReq.id.isEmpty()){
            throw new IllegalArgumentException("No se puede actualizar, el id de pago es requerido");
        }
        _paymentRepository.findById(paymentReq.id);
        var payment = Payment.create(
                paymentReq.id,
                paymentReq.product,
                paymentReq.discount,
                paymentReq.participant,
                paymentReq.campus,
                paymentReq.paymentshistory,
                paymentReq.total,
                paymentReq.status
        );
        _paymentRepository.save(payment);
    }
}
