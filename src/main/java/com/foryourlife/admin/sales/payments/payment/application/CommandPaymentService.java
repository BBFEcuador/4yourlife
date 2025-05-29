package com.foryourlife.admin.sales.payments.payment.application;

import com.foryourlife.admin.sales.invoices.application.QueryInvoiceService;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.admin.sales.payments.payment.infraestructure.httpControllers.PaymentRequest;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethodRepository;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.events.PaymentCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommandPaymentService {

    private final PaymentRepository _paymentRepository;
    private final QueryInvoiceService _queryInvoiceService;
    private final PaymentMethodRepository _paymentMethodRepository;

    private final EventBus eventBus;

    public CommandPaymentService(PaymentRepository _paymentRepository, QueryInvoiceService _queryInvoiceService, PaymentMethodRepository _paymentMethodRepository, EventBus eventBus) {
        this._paymentRepository = _paymentRepository;
        this._queryInvoiceService = _queryInvoiceService;
        this._paymentMethodRepository = _paymentMethodRepository;
        this.eventBus = eventBus;
    }

    public void save(PaymentRequest paymentReq) {

        boolean hasPendingPayments = _paymentRepository.existsByParticipantIdAndStatus(paymentReq.participant.getId(), PaymentStatus.PENDING);

        if (hasPendingPayments) {
            throw new BaseException("No se puede adquirir el servicio, existen pagos pendientes", List.of(""));
        }

        paymentReq.paymentshistory.forEach(paymentHistory -> {
            if (!_paymentMethodRepository.exist(paymentHistory.getPaymentMethodId())) {
                throw new BaseException("El método de pago no existe", List.of(""));
            }
        });

        var payment = Payment.create(
                paymentReq.id = UUID.randomUUID().toString(),
                paymentReq.product,
                paymentReq.discount,
                paymentReq.participant,
                paymentReq.campus,
                paymentReq.paymentshistory,
                paymentReq.total,
                paymentReq.status != null ? paymentReq.status : PaymentStatus.PENDING
        );

        payment.record(new PaymentCreated(payment, paymentReq.dataInvoice));
        var events = payment.pullDomainEvents();
        eventBus.publish(events);
        _paymentRepository.save(payment);


    }

    public void update(PaymentRequest paymentReq) {
        if (paymentReq.id == null || paymentReq.id.isEmpty()) {
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

    public void updatePaymentsHistory(PaymentHistory paymentHistory, String paymentId) {
        var payment = _paymentRepository.findById(paymentId);
        var total = payment.getPaymentshistory().stream().mapToDouble(PaymentHistory::getAmount).sum() + paymentHistory.getAmount();
        if (total > payment.getTotal()) {
            throw new BaseException("El pago no puede superar el total de la compra", List.of(""));
        } else if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new BaseException("No se puede actualizar, el pago ya ha sido completado", List.of(""));
        } else if (total == payment.getTotal() && payment.getStatus() != PaymentStatus.COMPLETED) {
            payment.setStatus(PaymentStatus.COMPLETED);
        }

        payment.getPaymentshistory().add(paymentHistory);
        System.out.println("waos");

        var originalInvoice = _queryInvoiceService.findByPaymentId(paymentId);


        payment.record(new PaymentCreated(payment, originalInvoice.getDataInvoice()));

        var events = payment.pullDomainEvents();
        eventBus.publish(events);
        var savedPayment = _paymentRepository.save(payment);
    }

}
