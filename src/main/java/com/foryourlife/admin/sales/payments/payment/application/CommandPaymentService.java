package com.foryourlife.admin.sales.payments.payment.application;

import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import com.foryourlife.admin.sales.invoices.application.QueryInvoiceService;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.infrastructure.http.InvoiceRequest;
import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerQueryService;
import com.foryourlife.admin.sales.payments.cashDrawerDetail.application.CashDrawerDetailCommandService;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.admin.sales.payments.payment.infrastructure.httpControllers.PaymentRequest;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethodRepository;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.clients.account.participant.application.ParticipantQueryService;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.events.PaymentCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommandPaymentService {

    private final PaymentRepository _paymentRepository;
    private final PaymentMethodRepository _paymentMethodRepository;
    private final ProductRepository _productRepository;
    private final ParticipantQueryService participantQueryService;
    private final QueryCampusService queryCampusService;
    private final EventBus eventBus;
    private final CashDrawerDetailCommandService cashDrawerDetailCommandService;
    private final CashDrawerQueryService cashDrawerQueryService;

    public CommandPaymentService(PaymentRepository _paymentRepository, PaymentMethodRepository _paymentMethodRepository, ProductRepository _productRepository, ParticipantQueryService participantQueryService, QueryCampusService queryCampusService, EventBus eventBus, CashDrawerDetailCommandService cashDrawerDetailCommandService, CashDrawerQueryService cashDrawerQueryService) {
        this._paymentRepository = _paymentRepository;
        this._paymentMethodRepository = _paymentMethodRepository;
        this._productRepository = _productRepository;
        this.participantQueryService = participantQueryService;
        this.queryCampusService = queryCampusService;
        this.eventBus = eventBus;
        this.cashDrawerDetailCommandService = cashDrawerDetailCommandService;
        this.cashDrawerQueryService = cashDrawerQueryService;
    }

    public void save(PaymentRequest paymentReq) {

        boolean hasPendingPayments = _paymentRepository.existsByParticipantIdAndStatus(paymentReq.participant, PaymentStatus.PENDING);
        var participant = participantQueryService.getUserById(paymentReq.participant);

        if (hasPendingPayments) {
            throw new BaseException("No se puede adquirir el servicio, existen pagos pendientes", List.of(""));
        }

        paymentReq.paymentsHistory.forEach(paymentHistory -> {
            if (!_paymentMethodRepository.exist(paymentHistory.getPaymentMethodId())) {
                throw new BaseException("El método de pago no existe", List.of(""));
            }
        });

        if (participant.getModules().getHasFocus() || participant.getModules().getHasYour() || participant.getModules().getHasLife()) {
            throw new BaseException("El participante ya tiene un módulo activo", List.of(""));
        }
        var total = paymentReq.paymentsHistory.stream().mapToDouble(PaymentHistory::getAmount).sum();

        if (total > paymentReq.total) {
            throw new BaseException("El pago no puede superar el total de la compra", List.of(""));

        } else if (total == paymentReq.total) {
            paymentReq.status = PaymentStatus.COMPLETED;
        }

        var products =
                paymentReq.products.stream().map(productId -> {
                    return _productRepository.findById(productId).orElseThrow(
                            () -> new BaseException("Producto no encontrado", List.of(""))
                    );
                }).collect(Collectors.toList());

        var payment = Payment.create(
                UUID.randomUUID().toString(),
                products,
                paymentReq.discount,
                participant,
                queryCampusService.findById(paymentReq.campus),
                paymentReq.paymentsHistory,
                paymentReq.total,
                paymentReq.status != null ? paymentReq.status : PaymentStatus.PENDING,
                paymentReq.note
        );

        var invoice = Invoice.create(
                UUID.randomUUID().toString(),
                paymentReq.invoice.fullName,
                paymentReq.invoice.address,
                paymentReq.invoice.document,
                paymentReq.invoice.phone,
                paymentReq.invoice.email,
                paymentReq.invoice.invoiceNumber,
                LocalDate.now(),
                products,
                payment,
                false,
                (paymentReq.total * 0.15),
                15.0,
                paymentReq.total
        );

        String paymentHistoryId = null;
        if (!payment.getPaymentshistory().isEmpty()) {

            payment.getPaymentshistory().getFirst().setId(UUID.randomUUID().toString());
            paymentHistoryId = payment.getPaymentshistory().getFirst().getId();
            payment.getPaymentshistory().getFirst().setId(UUID.randomUUID().toString());

        }

        _paymentRepository.save(payment);
        payment.record(new PaymentCreated(payment, invoice));
        var events = payment.pullDomainEvents();
        eventBus.publish(events);

        cashDrawerDetailCommandService.save(paymentHistoryId, paymentReq.cashDrawerId, payment);
    }

    public void update(PaymentRequest paymentReq) {
        if (paymentReq.id == null || paymentReq.id.isEmpty()) {
            throw new IllegalArgumentException("No se puede actualizar, el id de pago es requerido");
        }
        var participant = participantQueryService.getUserById(paymentReq.participant);
        List<Product> products = new ArrayList<>(List.of());
        paymentReq.products.forEach(
                productId -> {
                    var product = _productRepository.findById(productId).orElseThrow(
                            () -> new BaseException("Producto no encontrado", List.of(""))
                    );

                    products.add(product);
                }
        );
        _paymentRepository.findById(paymentReq.id);
        var payment = Payment.create(
                paymentReq.id,
                products,
                paymentReq.discount,
                participant,
                queryCampusService.findById(paymentReq.campus),
                paymentReq.paymentsHistory,
                paymentReq.total,
                paymentReq.status,
                paymentReq.note
        );
        _paymentRepository.save(payment);
    }

    public void updatePaymentsHistory(PaymentHistory paymentHistory, InvoiceRequest invoiceRequest, String paymentId, String cashDrawerId) {
        var payment = _paymentRepository.findById(paymentId);
        if (!_paymentMethodRepository.exist(paymentHistory.getPaymentMethodId())) {
            throw new BaseException("El método de pago no existe", List.of(""));
        }
        var total = payment.getPaymentshistory().stream().mapToDouble(PaymentHistory::getAmount).sum() + paymentHistory.getAmount();
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new BaseException("No se puede actualizar, el pago ya ha sido completado", List.of(""));

        } else if (total > payment.getTotal()) {
            throw new BaseException("El pago no puede superar el total de la compra: ", List.of(
                    "el total de pagos es: " + total + ", el total de la compra es: " + payment.getTotal()));

        } else if (total == payment.getTotal() && payment.getStatus() != PaymentStatus.COMPLETED) {
            payment.setStatus(PaymentStatus.COMPLETED);
        }
        paymentHistory.setId(UUID.randomUUID().toString());
        payment.getPaymentshistory().add(paymentHistory);


        var cashDrawer = cashDrawerQueryService.getCashDrawerById(cashDrawerId);

        cashDrawerDetailCommandService.save(paymentHistory.getId(), cashDrawer.getId(), payment);

        _paymentRepository.save(payment);
    }

    public void changeStatus(String id, String status) {
        var payment = _paymentRepository.findById(id);
        if (payment == null) {
            throw new BaseException("Pago no encontrado", List.of(""));
        }
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BaseException("Estado de pago inválido", List.of(""));
        }

        if (payment.getStatus() == PaymentStatus.COMPLETED && paymentStatus != PaymentStatus.CANCELLED) {
            throw new BaseException("No se puede cambiar el estado de un pago completado", List.of(""));
        }

        payment.setStatus(paymentStatus);
        _paymentRepository.save(payment);
    }
}