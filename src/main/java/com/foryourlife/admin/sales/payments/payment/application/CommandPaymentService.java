package com.foryourlife.admin.sales.payments.payment.application;

import com.foryourlife.admin.contifico.config.application.ConfigContificoQueryService;
import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
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
import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.clients.account.invitations.applications.QueryInvitationServices;
import com.foryourlife.clients.account.module.application.ClientModuleCreatorService;
import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.participant.application.ParticipantQueryService;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participantLevel.application.ParticipantLevelService;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.events.PaymentCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommandPaymentService {

    private final PaymentRepository _paymentRepository;
    private final PaymentMethodRepository _paymentMethodRepository;
    private final ProductRepository _productRepository;
    private final ParticipantQueryService participantQueryService;
    private final QueryCampusService queryCampusService;
    private final QueryInvoiceService queryInvoiceService;
    private final CashDrawerDetailCommandService cashDrawerDetailCommandService;
    private final CashDrawerQueryService cashDrawerQueryService;
    private final ConfigContificoQueryService configContificoQueryService;
    private final CommandInvoiceService commandInvoiceService;
    private final ClientModuleCreatorService clientModuleCreatorService;
    private final QueryInvitationServices queryInvitationServices;
    private final PromiseRepository promiseRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantLevelService participantLevelRepository;
    private final TrainingRepository trainingRepository;

    public CommandPaymentService(PaymentRepository _paymentRepository, PaymentMethodRepository _paymentMethodRepository, ProductRepository _productRepository, ParticipantQueryService participantQueryService, QueryCampusService queryCampusService, QueryInvoiceService queryInvoiceService, CashDrawerDetailCommandService cashDrawerDetailCommandService, CashDrawerQueryService cashDrawerQueryService, ConfigContificoQueryService configContificoQueryService, CommandInvoiceService commandInvoiceService, ClientModuleCreatorService clientModuleCreatorService, QueryInvitationServices queryInvitationServices, PromiseRepository promiseRepository, ParticipantRepository participantRepository, ParticipantLevelService participantLevelRepository, TrainingRepository trainingRepository) {
        this._paymentRepository = _paymentRepository;
        this._paymentMethodRepository = _paymentMethodRepository;
        this._productRepository = _productRepository;
        this.participantQueryService = participantQueryService;
        this.queryCampusService = queryCampusService;
        this.queryInvoiceService = queryInvoiceService;
        this.cashDrawerDetailCommandService = cashDrawerDetailCommandService;
        this.cashDrawerQueryService = cashDrawerQueryService;
        this.configContificoQueryService = configContificoQueryService;
        this.commandInvoiceService = commandInvoiceService;
        this.clientModuleCreatorService = clientModuleCreatorService;
        this.queryInvitationServices = queryInvitationServices;
        this.promiseRepository = promiseRepository;
        this.participantRepository = participantRepository;
        this.participantLevelRepository = participantLevelRepository;
        this.trainingRepository = trainingRepository;
    }

    @Transactional
    public String save(PaymentRequest paymentReq) {

        boolean hasPendingPayments = _paymentRepository.existsByParticipantIdAndStatus(paymentReq.participant, PaymentStatus.PENDING);
        var participant = participantQueryService.getParticipantById(paymentReq.participant);

        if (hasPendingPayments) {
            throw new BaseException("No se puede adquirir el servicio, existen pagos pendientes", List.of(""));
        }

        paymentReq.paymentsHistory.forEach(paymentHistory -> {
            if (!_paymentMethodRepository.exist(paymentHistory.getPaymentMethod().getId())) {
                throw new BaseException("El método de pago no existe", List.of(""));
            }
        });

        var total = BigDecimal.valueOf(paymentReq.paymentsHistory.stream().mapToDouble(PaymentHistory::getAmount).sum());

        if (total.compareTo(paymentReq.total) > 0) {
            throw new BaseException("El pago no puede superar el total de la compra", List.of(""));

        } else if (total.equals(paymentReq.total)) {
            paymentReq.status = PaymentStatus.COMPLETED;
        }

        List<Product> products = paymentReq.products.stream().map(productId -> {
            return _productRepository.findById(productId).orElseThrow(() -> new BaseException("Producto no encontrado", List.of("")));
        }).collect(Collectors.toList());
        EnumSet<CourseLevel> activeLevels = EnumSet.noneOf(CourseLevel.class);
        var modules = participant.getModules();
        if (modules != null) {
            if (Boolean.TRUE.equals(modules.getHasFocus())) activeLevels.add(CourseLevel.FOCUS);
            if (Boolean.TRUE.equals(modules.getHasYour())) activeLevels.add(CourseLevel.YOUR);
            if (Boolean.TRUE.equals(modules.getHasLife())) activeLevels.add(CourseLevel.LIFE);
        } else {
            ClientModule clientModule = new ClientModule(
                    UUID.randomUUID().toString(),
                    false,
                    false,
                    false,
                    participant
            );
            clientModuleCreatorService.createClientModule(clientModule);
        }

        for (Product product : products) {
            var programs = product.getPrograms() != null ? product.getPrograms() : List.<Program>of();

            for (Program program : programs) {
                var level = program.getCourseLevel();
                if (level != null && activeLevels.contains(level)) {
                    String msg = "El participante ya tiene activo un programa de nivel " + level;
                    throw new BaseException(msg, List.of(""));
                }
            }
        }

        BigDecimal remaining = paymentReq.total.subtract(total);
        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            paymentReq.status = PaymentStatus.COMPLETED;
        }

        var training = trainingRepository.findById(paymentReq.trainingId).orElseThrow(
                () -> new BaseException("Entrenamiento no encontrado", List.of("El entrenamiento con id " + paymentReq.trainingId + " no existe"))
        );

        var payment = Payment.create(
                UUID.randomUUID().toString(),
                products,
                paymentReq.discount,
                participant,
                queryCampusService.findById(paymentReq.campus),
                paymentReq.paymentsHistory,
                paymentReq.total,
                paymentReq.status != null ? paymentReq.status : PaymentStatus.PENDING,
                paymentReq.note,
                training
        );
        _paymentRepository.save(payment);

        BigDecimal divisor = BigDecimal.valueOf(1.15);
        BigDecimal taxAmount = total.subtract(total.divide(divisor, 2, RoundingMode.DOWN));

        if (!paymentReq.paymentsHistory.isEmpty()) {
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
                    taxAmount.setScale(2, RoundingMode.DOWN),
                    paymentReq.totalDiscount.setScale(2, RoundingMode.DOWN),
                    15.0,
                    total.setScale(2, RoundingMode.DOWN),
                    paymentReq.invoice.type
            );

            payment.getPaymentshistory().forEach(paymentHistory -> {
                cashDrawerDetailCommandService.save(paymentHistory.getId(), paymentReq.cashDrawerId, payment);
            });
            var cashDrawer = cashDrawerQueryService.getCashDrawerById(paymentReq.cashDrawerId);
            _paymentRepository.save(payment);
            var newInvoice = createInvoice(invoice, cashDrawer, payment, payment.getPaymentshistory());
            this.paymentCreated(new PaymentCreated(payment, newInvoice, cashDrawer));
        }

        cashDrawerDetailCommandService.save(null, paymentReq.cashDrawerId, payment);

        return payment.getId();
    }

    @Transactional
    public Invoice createInvoice(Invoice invoice, CashDrawer cashDrawer, Payment payment, List<PaymentHistory> paymentHistories) {
        try {
            String invoiceNumber = getNextInvoiceNumber(cashDrawer);
            invoice.setInvoiceNumber(invoiceNumber);

            var config = configContificoQueryService.findConfigContificoByCampusId(payment.getCampus().getId());
            if (config == null) return commandInvoiceService.save(invoice);

            String storeCode = cashDrawer.getCashBox().getStore().getNumber();
            String cashBoxCode = cashDrawer.getCashBox().getNumber();
            String formattedInvoiceDate = invoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            InvoiceContificoJson.Cliente client = new InvoiceContificoJson.Cliente(
                    invoice.getDocument(),
                    invoice.getFullName(),
                    invoice.getPhone(),
                    invoice.getAddress(),
                    invoice.getClientType(),
                    invoice.getEmail()
            );

            BigDecimal totalAmount = invoice.getAmount();
            BigDecimal taxAmount = invoice.getTaxAmount();
            BigDecimal subtotal = totalAmount.subtract(taxAmount).setScale(2, RoundingMode.DOWN);

            List<InvoiceContificoJson.Detalle> details = buildProductDetails(payment, subtotal);

            List<InvoiceContificoJson.Cobros> cobros = buildCobros(paymentHistories, formattedInvoiceDate);

            String sequential = storeCode + "-" + cashBoxCode + "-" + invoiceNumber;

            InvoiceContificoJson json = new InvoiceContificoJson(
                    config.getApiKey(),
                    formattedInvoiceDate,
                    "FAC",
                    sequential,
                    "",
                    client,
                    BigDecimal.ZERO,
                    subtotal,
                    BigDecimal.ZERO,
                    taxAmount,
                    totalAmount,
                    invoice.getPayment().getNote(),
                    details,
                    BigDecimal.ZERO,
                    "Generado en 4YourLife",
                    "P",
                    true,
                    cobros
            );

            adjustRucIfNeeded(json);
            invoice.setInvoiceContifico(json);

            Invoice saved = commandInvoiceService.save(invoice);
            commandInvoiceService.sendInvoiceToContifico(saved);

            return saved;

        } catch (Exception e) {
            e.printStackTrace();
            return invoice;
        }
    }

    private List<InvoiceContificoJson.Cobros> buildCobros(List<PaymentHistory> paymentHistories, String formattedDate) {
        Set<String> unique = new HashSet<>();
        List<InvoiceContificoJson.Cobros> result = new ArrayList<>();

        paymentHistories.forEach(sh -> {
            String key = sh.getPaymentMethod().getCode() + "|" + sh.getAmount() + "|" + sh.getTransactionId();

            if (unique.contains(key)) return;
            unique.add(key);

            InvoiceContificoJson.Cobros c = new InvoiceContificoJson.Cobros();

            c.setForma_cobro(sh.getPaymentMethod().getCode());
            c.setMonto(BigDecimal.valueOf(sh.getAmount()));
            c.setFecha(formattedDate);

            if (sh.getPaymentMethod().getCode().equals("TRA")) {
                c.setCuenta_bancaria_id(sh.getPaymentMethod().getBank().getContificoId());
                c.setNumero_comprobante(sh.getTransactionId());
            }
            if (sh.getPaymentMethod().getCode().equals("CHE")) {
                c.setNumero_cheque(sh.getTransactionId());
            }
            if (sh.getPaymentMethod().getCode().equals("TC")) {
                c.setTipo_ping("D");
            }

            result.add(c);
        });

        return result;
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
        BigDecimal productSubtotal = subtotal.divide(BigDecimal.valueOf(productCount), 2, RoundingMode.DOWN);

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

    @Transactional
    public void updatePaymentsHistory(PaymentHistory paymentHistory, String paymentId, String cashDrawerId) {
        var payment = _paymentRepository.findById(paymentId);

        if (!_paymentMethodRepository.exist(paymentHistory.getPaymentMethod().getId())) {
            throw new BaseException("El método de pago no existe", List.of(""));
        }

        double currentTotal = payment.getPaymentshistory().stream()
                .mapToDouble(PaymentHistory::getAmount)
                .sum();
        double newTotal = currentTotal + paymentHistory.getAmount();

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new BaseException("No se puede actualizar, el pago ya ha sido completado", List.of(""));
        }

        if (BigDecimal.valueOf(newTotal).compareTo(payment.getTotal()) > 0) {
            throw new BaseException(
                    "El pago no puede superar el total de la compra",
                    List.of("Total actual: " + newTotal + ", Total de la compra: " + payment.getTotal())
            );
        }

        if (BigDecimal.valueOf(newTotal).compareTo(payment.getTotal()) == 0 && payment.getStatus() != PaymentStatus.COMPLETED) {
            payment.setStatus(PaymentStatus.COMPLETED);
        }

        boolean duplicateExists = payment.getPaymentshistory().stream()
                .anyMatch(h -> h.getTransactionId().equals(paymentHistory.getTransactionId()));

        if (duplicateExists) {
            throw new BaseException(
                    "El pago con el código de transacción: " + paymentHistory.getTransactionId() + " ya existe",
                    List.of("")
            );
        }

        var originalInvoice = queryInvoiceService.getByPaymentId(paymentId);
        BigDecimal amount = BigDecimal.valueOf(paymentHistory.getAmount());
        BigDecimal divisor = BigDecimal.valueOf(1.15);
        BigDecimal taxAmount = amount.subtract(amount.divide(divisor, 2, RoundingMode.DOWN));

        var newInvoice = Invoice.create(
                UUID.randomUUID().toString(),
                originalInvoice.getFullName(),
                originalInvoice.getAddress(),
                originalInvoice.getDocument(),
                originalInvoice.getPhone(),
                originalInvoice.getEmail(),
                originalInvoice.getInvoiceNumber(),
                LocalDateTime.now(),
                originalInvoice.getProducts(),
                payment,
                false,
                taxAmount,
                BigDecimal.ZERO,
                15.0,
                amount,
                originalInvoice.getClientType()
        );

        paymentHistory.setId(UUID.randomUUID().toString());
        paymentHistory.setSent(false);
        payment.getPaymentshistory().add(paymentHistory);

        _paymentRepository.save(payment);

        var cashDrawer = cashDrawerQueryService.getCashDrawerById(cashDrawerId);
        cashDrawerDetailCommandService.save(paymentHistory.getId(), cashDrawer.getId(), payment);

        createInvoice(newInvoice, cashDrawer, payment, List.of(paymentHistory));
        this.paymentCreated(new PaymentCreated(payment, newInvoice, cashDrawer));
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
            return pdf;
        } catch (Exception e) {
            throw new BaseException("Error generating invoice", List.of(e.getMessage()));
        }
    }

    public void paymentCreated(PaymentCreated event) {
        if (event.getPayment().getStatus() == PaymentStatus.COMPLETED) {
            Participant participant = event.getPayment().getParticipant();
            event.getPayment().getProducts().forEach(product -> {

                var invitation = queryInvitationServices.findInvitationByToken(participant.getInvitationToken());

                var promiseOpt = promiseRepository.findLastByUserId(invitation.getSenderId());

                if (promiseOpt.isPresent()) {
                    var promise = promiseOpt.get();

                    LocalDate createdDate = participant.getCreatedDate().toLocalDate();
                    LocalDate startDate = promise.getStartDate();
                    LocalDate endDate = promise.getEndDate();

                    if ((createdDate.isEqual(startDate) || createdDate.isAfter(startDate)) &&
                            (createdDate.isEqual(endDate) || createdDate.isBefore(endDate))) {

                        promise.setPaidCount(promise.getPaidCount() + 1);
                        promiseRepository.save(promise);
                    }
                }
                var prod = event.getPayment().getProducts().getFirst();

                prod.getPrograms().forEach(program -> {
                    switch (program.getCourseLevel()) {
                        case FOCUS -> participant.getModules().setHasFocus(true);
                        case YOUR -> participant.getModules().setHasYour(true);
                        case LIFE -> participant.getModules().setHasLife(true);
                        default -> throw new BaseException("Invalid course level", List.of(""));
                    }
                });
                if (event.getPayment().getParticipant().getParticipantLevel().getCourseLevel() == CourseLevel.INIT) {
                    participant.setOriginalTraining(event.getPayment().getTraining().getName());
                    var pl = participantLevelRepository.getRolByLevel(CourseLevel.FOCUS);
                    participant.setParticipantLevel(pl);
                    participantRepository.save(participant);
                }
            });
            clientModuleCreatorService.createClientModule(participant.getModules());
        }
    }
}