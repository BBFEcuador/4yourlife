package com.foryourlife.admin.sales.payments.payment.application;

import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import com.foryourlife.admin.sales.invoices.application.QueryInvoiceService;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import com.foryourlife.admin.sales.payments.payment.infraestructure.httpControllers.PaymentRequest;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethod;
import com.foryourlife.admin.sales.payments.paymentMethod.domain.PaymentMethodRepository;
import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.admin.sales.product.domain.ProductRepository;
import com.foryourlife.clients.account.invoiceData.domain.InvoiceDataRepository;
import com.foryourlife.clients.account.participant.application.ParticipantQueryService;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.events.PaymentCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommandPaymentService {

    private final PaymentRepository _paymentRepository;
    private final QueryInvoiceService _queryInvoiceService;
    private final PaymentMethodRepository _paymentMethodRepository;
    private final ProductRepository _productRepository;
    private final ParticipantQueryService participantQueryService;
    private final QueryCampusService queryCampusService;
    private final EventBus eventBus;

    public CommandPaymentService(PaymentRepository _paymentRepository, QueryInvoiceService _queryInvoiceService, PaymentMethodRepository _paymentMethodRepository, ProductRepository _productRepository, ParticipantQueryService participantQueryService, QueryCampusService queryCampusService, EventBus eventBus) {
        this._paymentRepository = _paymentRepository;
        this._queryInvoiceService = _queryInvoiceService;
        this._paymentMethodRepository = _paymentMethodRepository;
        this._productRepository = _productRepository;
        this.participantQueryService = participantQueryService;
        this.queryCampusService = queryCampusService;
        this.eventBus = eventBus;
    }

    public void save(PaymentRequest paymentReq) {

        boolean hasPendingPayments = _paymentRepository.existsByParticipantIdAndStatus(paymentReq.participant, PaymentStatus.PENDING);
        var participant = participantQueryService.getUserById(paymentReq.participant);

        if (hasPendingPayments) {
            throw new BaseException("No se puede adquirir el servicio, existen pagos pendientes", List.of(""));
        }

        paymentReq.paymentshistory.forEach(paymentHistory -> {
            if (!_paymentMethodRepository.exist(paymentHistory.getPaymentMethodId())) {
                throw new BaseException("El método de pago no existe", List.of(""));
            }
        });

        if (participant.getModules().getHasFocus() || participant.getModules().getHasYour() || participant.getModules().getHasLife()) {
            throw new BaseException("El participante ya tiene un módulo activo", List.of(""));
        }
        var total = paymentReq.paymentshistory.stream().mapToDouble(PaymentHistory::getAmount).sum();

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
                paymentReq.id = UUID.randomUUID().toString(),
                products,
                paymentReq.discount,
                participant,
                queryCampusService.findById(paymentReq.campus),
                paymentReq.paymentshistory,
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
                false
        );

        _paymentRepository.save(payment);
        payment.record(new PaymentCreated(payment, invoice));
        var events = payment.pullDomainEvents();
        eventBus.publish(events);
    }

    public void update(PaymentRequest paymentReq) {
        if (paymentReq.id == null || paymentReq.id.isEmpty()) {
            throw new IllegalArgumentException("No se puede actualizar, el id de pago es requerido");
        }
        var participant = participantQueryService.getUserById(paymentReq.participant);
        List<Product> products = List.of();
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
                paymentReq.paymentshistory,
                paymentReq.total,
                paymentReq.status,
                paymentReq.note
        );
        _paymentRepository.save(payment);
    }

    public void updatePaymentsHistory(PaymentHistory paymentHistory, String paymentId) {
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

        payment.getPaymentshistory().add(paymentHistory);

        var originalInvoice = _queryInvoiceService.findByPaymentId(paymentId).getFirst();

        _paymentRepository.save(payment);
        payment.record(new PaymentCreated(payment, originalInvoice));

        var events = payment.pullDomainEvents();
        eventBus.publish(events);
    }

}
