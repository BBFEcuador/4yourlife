package com.foryourlife.admin.sales.payments.payment.application;

import com.foryourlife.admin.contifico.config.application.ConfigContificoQueryService;
import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import com.foryourlife.admin.sales.invoices.application.CommandInvoiceService;
import com.foryourlife.admin.sales.invoices.application.QueryInvoiceService;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceContificoJson;
import com.foryourlife.admin.sales.payments.cashDrawer.application.CashDrawerQueryService;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
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
import com.foryourlife.shared.domain.events.PaymentHistoryCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final QueryInvoiceService queryInvoiceService;
    private final EventBus eventBus;
    private final CashDrawerDetailCommandService cashDrawerDetailCommandService;
    private final CashDrawerQueryService cashDrawerQueryService;
    private final ConfigContificoQueryService configContificoQueryService;
    private final CommandInvoiceService commandInvoiceService;

    public CommandPaymentService(PaymentRepository _paymentRepository, PaymentMethodRepository _paymentMethodRepository, ProductRepository _productRepository, ParticipantQueryService participantQueryService, QueryCampusService queryCampusService, QueryInvoiceService queryInvoiceService, EventBus eventBus, CashDrawerDetailCommandService cashDrawerDetailCommandService, CashDrawerQueryService cashDrawerQueryService, ConfigContificoQueryService configContificoQueryService, CommandInvoiceService commandInvoiceService) {
        this._paymentRepository = _paymentRepository;
        this._paymentMethodRepository = _paymentMethodRepository;
        this._productRepository = _productRepository;
        this.participantQueryService = participantQueryService;
        this.queryCampusService = queryCampusService;
        this.queryInvoiceService = queryInvoiceService;
        this.eventBus = eventBus;
        this.cashDrawerDetailCommandService = cashDrawerDetailCommandService;
        this.cashDrawerQueryService = cashDrawerQueryService;
        this.configContificoQueryService = configContificoQueryService;
        this.commandInvoiceService = commandInvoiceService;
    }

    public String save(PaymentRequest paymentReq) {

        boolean hasPendingPayments = _paymentRepository.existsByParticipantIdAndStatus(paymentReq.participant, PaymentStatus.PENDING);
        var participant = participantQueryService.getUserById(paymentReq.participant);

        if (hasPendingPayments) {
            throw new BaseException("No se puede adquirir el servicio, existen pagos pendientes", List.of(""));
        }

        paymentReq.paymentsHistory.forEach(paymentHistory -> {
            if (!_paymentMethodRepository.exist(paymentHistory.getPaymentMethod().getId())) {
                throw new BaseException("El método de pago no existe", List.of(""));
            }
        });

        var total = paymentReq.paymentsHistory.stream().mapToDouble(PaymentHistory::getAmount).sum();

        if (total > paymentReq.total) {
            throw new BaseException("El pago no puede superar el total de la compra", List.of(""));

        } else if (total == paymentReq.total) {
            paymentReq.status = PaymentStatus.COMPLETED;
        }

        List<Product> products = paymentReq.products.stream().map(productId -> {
            return _productRepository.findById(productId).orElseThrow(() -> new BaseException("Producto no encontrado", List.of("")));
        }).collect(Collectors.toList());

        products.forEach(product -> {
            if (participant.getModules().getHasFocus() || participant.getModules().getHasYour() || participant.getModules().getHasLife()) {
                throw new BaseException("El participante ya tiene un módulo activo", List.of(""));
            }
        });

        var payment = Payment.create(UUID.randomUUID().toString(), products, paymentReq.discount, participant, queryCampusService.findById(paymentReq.campus), paymentReq.paymentsHistory, paymentReq.total, paymentReq.status != null ? paymentReq.status : PaymentStatus.PENDING, paymentReq.note);

        BigDecimal totalProduct = BigDecimal.valueOf(paymentReq.total);
        BigDecimal divisor = BigDecimal.valueOf(1.15);
        BigDecimal taxAmount = totalProduct.subtract(totalProduct.divide(divisor, 2, RoundingMode.HALF_UP));

        var invoice = Invoice.create(
                UUID.randomUUID().toString(),
                paymentReq.invoice.fullName,
                paymentReq.invoice.address,
                paymentReq.invoice.document,
                paymentReq.invoice.phone,
                paymentReq.invoice.email,
                paymentReq.invoice.invoiceNumber,
                LocalDateTime.now(),
                products,
                payment,
                false,
                taxAmount.doubleValue(),
                paymentReq.totalDiscount,
                15.0,
                paymentReq.total,
                paymentReq.invoice.type);
        String paymentHistoryId = null;

        var cashDrawer = cashDrawerQueryService.getCashDrawerById(paymentReq.cashDrawerId);
        _paymentRepository.save(payment);
        var newInvoice = createInvoice(invoice, cashDrawer, payment);
        if (!payment.getPaymentshistory().isEmpty()) {

            payment.getPaymentshistory().forEach(
                    paymentHistory -> {
                    paymentHistory.setId(UUID.randomUUID().toString());
                    }
            );

            if (newInvoice.getContificoId() != null) {
                payment.getPaymentshistory().forEach(
                        paymentHistory -> {
                            PaymentHistoryCreated event = new PaymentHistoryCreated(paymentHistory, newInvoice);
                            eventBus.publish(List.of(event));
                        }
                );
            }
        }
        _paymentRepository.save(payment);
        cashDrawerDetailCommandService.save(paymentHistoryId, paymentReq.cashDrawerId, payment);
        eventBus.publish(List.of(new PaymentCreated(payment, invoice, cashDrawer)));
        return payment.getId();
    }

    public Invoice createInvoice(Invoice invoice, CashDrawer cashDrawer, Payment payment) {
        if (invoice == null) {
            throw new BaseException("No se puede crear factura: el campo dataInvoice está vacío.", List.of(""));
        }
        try {
            String invoiceNumber = getNextInvoiceNumber(cashDrawer);

            invoice.setInvoiceNumber(invoiceNumber);

            // Obtener configuración Contifico
            var config = configContificoQueryService.findConfigContificoByCampusId(payment.getCampus().getId());
            if (config == null) {
                System.err.println("No se encontró la configuración de Contifico para el campus.");
                return commandInvoiceService.save(invoice);
            }

            // Generar autorización
            String formattedDate = invoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            String storeCode = cashDrawer.getCashBox().getStore().getNumber();
            String cashBoxCode = cashDrawer.getCashBox().getNumber();
            String authorization = formattedDate + "01" + config.getRuc() + "2" + storeCode + cashBoxCode + invoiceNumber + "12345678" + "1";
            var verificationDigit = generateModule(authorization);

            // Cliente Contifico
            var client = new InvoiceContificoJson.Cliente(invoice.getDocument(), invoice.getFullName(), invoice.getPhone(), invoice.getAddress(), invoice.getClientType(), invoice.getEmail());

            // Subtotales
            BigDecimal totalAmount = BigDecimal.valueOf(invoice.getAmount());
            BigDecimal taxAmount = BigDecimal.valueOf(invoice.getTaxAmount());
            BigDecimal subtotal = totalAmount.subtract(taxAmount).setScale(2, RoundingMode.HALF_UP);

            // Detalles del producto
            List<InvoiceContificoJson.Detalle> productDetails = buildProductDetails(payment, subtotal);

            // Crear objeto Contifico
            String formattedInvoiceDate = invoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String sequential = storeCode + "-" + cashBoxCode + "-" + invoiceNumber;
            String claveAcceso = authorization + verificationDigit;

            var invoiceContifico = new InvoiceContificoJson(config.getApiKey(), formattedInvoiceDate, "FAC", sequential, claveAcceso, client, 0, subtotal.doubleValue(), 0, taxAmount.doubleValue(), totalAmount.doubleValue(), productDetails, 0, "Generado en 4YourLife", "P");

            // Ajustar RUC si aplica
            adjustRucIfNeeded(invoiceContifico);

            invoice.setInvoiceContifico(invoiceContifico);

            // Guardar y enviar
            Invoice savedInvoice = commandInvoiceService.save(invoice);
            commandInvoiceService.sendInvoiceToContifico(savedInvoice);

            System.out.println("Factura creada y enviada correctamente.");

            return savedInvoice;
        } catch (Exception e) {
            System.err.println("Error al crear o guardar la factura: " + e.getMessage());
            e.printStackTrace();
        }
        return invoice;
    }

    public static int generateModule(String claveAcceso) {
        int factor = 2;
        int suma = 0;
        for (int i = claveAcceso.length() - 1; i >= 0; i--) {
            suma += factor * Character.getNumericValue(claveAcceso.charAt(i));
            factor = factor % 7 == 0 ? 2 : factor + 1;
        }
        int dv = 11 - suma % 11;
        return dv == 11 ? 0 : (dv == 10 ? 1 : dv);
    }

    private String getNextInvoiceNumber(CashDrawer cashDrawer) {
        try {
            Invoice lastInvoice = queryInvoiceService.findLastInvoice();
            int nextNumber = Integer.parseInt(lastInvoice.getInvoiceNumber()) + 1;
            return String.format("%09d", nextNumber);
        } catch (BaseException e) {
            System.out.println("No previous invoice found, starting with invoice #1");
            return String.format("%09d", cashDrawer.getCashBox().getFirstNumberInvoice());
        }
    }

    private List<InvoiceContificoJson.Detalle> buildProductDetails(Payment payment, BigDecimal subtotal) {
        List<InvoiceContificoJson.Detalle> details = new ArrayList<>();
        int productCount = payment.getProducts().size();
        BigDecimal productSubtotal = subtotal.divide(BigDecimal.valueOf(productCount), 2, RoundingMode.HALF_UP);

        for (var product : payment.getProducts()) {
            details.add(new InvoiceContificoJson.Detalle(product.getContificoId(), 1, productSubtotal.doubleValue(), 15, 0, 0, productSubtotal.doubleValue(), 0, 0, 0.0));
        }

        return details;
    }

    private void adjustRucIfNeeded(InvoiceContificoJson invoiceContifico) {
        if (invoiceContifico.cliente.cedula.length() == 13) {
            invoiceContifico.cliente.ruc = invoiceContifico.cliente.cedula;
            invoiceContifico.cliente.cedula = invoiceContifico.cliente.cedula.substring(0, 10);
        }
    }


    public void update(PaymentRequest paymentReq) {
        if (paymentReq.id == null || paymentReq.id.isEmpty()) {
            throw new IllegalArgumentException("No se puede actualizar, el id de pago es requerido");
        }
        var participant = participantQueryService.getUserById(paymentReq.participant);
        List<Product> products = new ArrayList<>(List.of());
        paymentReq.products.forEach(productId -> {
            var product = _productRepository.findById(productId).orElseThrow(() -> new BaseException("Producto no encontrado", List.of("")));
            products.add(product);
        });
        _paymentRepository.findById(paymentReq.id);
        var payment = Payment.create(paymentReq.id, products, paymentReq.discount, participant, queryCampusService.findById(paymentReq.campus), paymentReq.paymentsHistory, paymentReq.total, paymentReq.status, paymentReq.note);
        _paymentRepository.save(payment);
    }

    public void updatePaymentsHistory(PaymentHistory paymentHistory, String paymentId, String cashDrawerId) {
        var payment = _paymentRepository.findById(paymentId);
        if (!_paymentMethodRepository.exist(paymentHistory.getPaymentMethod().getId())) {
            throw new BaseException("El método de pago no existe", List.of(""));
        }
        var total = payment.getPaymentshistory().stream().mapToDouble(PaymentHistory::getAmount).sum() + paymentHistory.getAmount();
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new BaseException("No se puede actualizar, el pago ya ha sido completado", List.of(""));

        } else if (total > payment.getTotal()) {
            throw new BaseException("El pago no puede superar el total de la compra: ", List.of("el total de pagos es: " + total + ", el total de la compra es: " + payment.getTotal()));

        } else if (total == payment.getTotal() && payment.getStatus() != PaymentStatus.COMPLETED) {
            payment.setStatus(PaymentStatus.COMPLETED);
        }
        paymentHistory.setId(UUID.randomUUID().toString());
        paymentHistory.setSent(false);
        payment.getPaymentshistory().add(paymentHistory);

        var cashDrawer = cashDrawerQueryService.getCashDrawerById(cashDrawerId);

        var invoice = queryInvoiceService.getByPaymentId(paymentId);

        cashDrawerDetailCommandService.save(paymentHistory.getId(), cashDrawer.getId(), payment);

        _paymentRepository.save(payment);

        PaymentHistoryCreated event = new PaymentHistoryCreated(paymentHistory, invoice);

        eventBus.publish(List.of(event));
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

    public ByteArrayOutputStream generateInvoice(String paymentId) {
        var payment = _paymentRepository.findById(paymentId);
        ByteArrayOutputStream pdf = new ByteArrayOutputStream();

        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(_paymentRepository.generatePdf(payment));
            renderer.layout();
            renderer.createPDF(pdf);
            /*String fileName = "invoice_" + paymentId + ".pdf";
            String filePath = Paths.get("").toAbsolutePath().toString() + File.separator + fileName;

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(pdf.toByteArray());
            }*/
            return pdf;
        } catch (Exception e) {
            throw new BaseException("Error generating invoice", List.of(e.getMessage()));
        }
    }
}