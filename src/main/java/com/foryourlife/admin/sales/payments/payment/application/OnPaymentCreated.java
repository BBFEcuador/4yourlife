package com.foryourlife.admin.sales.payments.payment.application;

import com.foryourlife.admin.contifico.config.application.ConfigContificoQueryService;
import com.foryourlife.admin.sales.invoices.application.CommandInvoiceService;
import com.foryourlife.admin.sales.invoices.application.QueryInvoiceService;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceContificoJson;
import com.foryourlife.admin.sales.product.application.ProductFinderService;
import com.foryourlife.clients.account.module.application.ClientModuleCreatorService;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participantLevel.application.ParticipantLevelService;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.PaymentCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@DomainEventSubscriber(value = {PaymentCreated.class})
public class OnPaymentCreated {

    private final ProductFinderService productFinderService;
    private final ClientModuleCreatorService clientModuleCreatorService;
    private final CommandInvoiceService commandInvoiceService;
    private final QueryInvoiceService queryInvoiceService;
    private final ParticipantRepository participantRepository;
    private final ParticipantLevelService participantLevelRepository;
    private final ConfigContificoQueryService configContificoQueryService;

    public OnPaymentCreated(ProductFinderService productFinderService, ClientModuleCreatorService clientModuleCreatorService, CommandInvoiceService commandInvoiceService, QueryInvoiceService queryInvoiceService, ParticipantRepository participantRepository, ParticipantLevelService participantLevelRepository, ConfigContificoQueryService configContificoQueryService) {
        this.productFinderService = productFinderService;
        this.clientModuleCreatorService = clientModuleCreatorService;
        this.commandInvoiceService = commandInvoiceService;
        this.queryInvoiceService = queryInvoiceService;
        this.participantRepository = participantRepository;
        this.participantLevelRepository = participantLevelRepository;
        this.configContificoQueryService = configContificoQueryService;
    }

    @EventListener
    @Transactional
    public void on(PaymentCreated event) {
        Participant participant = participantRepository.findById(event.getPayment().getParticipant().getId()).orElseThrow(NullPointerException::new);
        event.getPayment().getProducts().forEach(product -> {
            var prod = productFinderService.findById(product.getId());

            prod.getPrograms().forEach(program -> {
                switch (program.getCourseLevel()) {
                    case FOCUS -> participant.getModules().setHasFocus(true);
                    case YOUR -> participant.getModules().setHasYour(true);
                    case LIFE -> participant.getModules().setHasLife(true);
                    default -> throw new BaseException("Invalid course level", List.of(""));
                }
            });
            if (event.getPayment().getParticipant().getParticipantLevel().getCourseLevel() == CourseLevel.INIT) {
                var p = event.getPayment().getParticipant();
                var pl = participantLevelRepository.getRolByLevel(CourseLevel.FOCUS);
                p.setParticipantLevel(pl);
                participantRepository.save(p);
            }
        });
        clientModuleCreatorService.createClientModule(participant.getModules());

//        createInvoice(event);
    }

    public void createInvoice(PaymentCreated event) {
        try {
            if (event.getInvoice() == null) {
                System.err.println("No se puede crear factura: el campo dataInvoice está vacío.");
                return;
            }

            Invoice lastInvoice = null;
            try {
                lastInvoice = queryInvoiceService.findLastInvoice();
            } catch (BaseException e) {
                System.out.println("No previous invoice found, starting with invoice #1");
            }

            String invoiceNumber;
            if (lastInvoice == null) {
                invoiceNumber = String.format("%09d", event.getCashDrawer().getCashBox().getFirstNumberInvoice());
            } else {
                int nextInvoiceNumber = Integer.parseInt(lastInvoice.getInvoiceNumber()) + 1;
                invoiceNumber = String.format("%09d", nextInvoiceNumber);
            }

            event.getInvoice().setInvoiceNumber(invoiceNumber);
            var config = configContificoQueryService.findConfigContificoByCampusId(event.getPayment().getCampus().getId());
            if (config == null) {
                System.out.println("Invoice created and saved successfully.");

                commandInvoiceService.save(event.getInvoice());

                System.err.println("No se encontró la configuración de Contifico para el campus.");
                return;
            }
            String formattedDate = event.getInvoice().getInvoiceDate().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            var authorization = formattedDate + "01" + config.getRuc() + "2" + event.getCashDrawer().getCashBox().getStore().getNumber() + event.getCashDrawer().getCashBox().getNumber() + invoiceNumber + "12345678" + "1";
            var verificationDigit = generateModule(authorization);
            System.out.println(authorization);

            var client = new InvoiceContificoJson.Cliente(
                    event.getInvoice().getDocument(),
                    event.getInvoice().getFullName(),
                    event.getInvoice().getPhone(),
                    event.getInvoice().getAddress(),
                    "N",
                    event.getInvoice().getEmail()
            );

            BigDecimal subtotal = BigDecimal.valueOf(event.getInvoice().getAmount()).subtract(BigDecimal.valueOf(event.getInvoice().getTaxAmount())).setScale(2, RoundingMode.HALF_UP);
            var listProducts = new ArrayList<InvoiceContificoJson.Detalle>();
            for (var product : event.getPayment().getProducts()) {
                BigDecimal productSubtotal = subtotal.divide(BigDecimal.valueOf(event.getPayment().getProducts().size()), 2, RoundingMode.HALF_UP);

                listProducts.add(
                        new InvoiceContificoJson.Detalle(
                                product.getContificoId(),
                                1,
                                productSubtotal.doubleValue(),
                                15,
                                0,
                                0,
                                productSubtotal.doubleValue(),
                                0,
                                0,
                                0.0
                        )
                );
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDateInvoice = event.getInvoice().getInvoiceDate().format(formatter);
            event.getInvoice().setInvoiceContifico(
                    new InvoiceContificoJson(
                            config.getApiKey(),
                            formattedDateInvoice,
                            "FAC",
                            event.getCashDrawer().getCashBox().getStore().getNumber() + "-" + event.getCashDrawer().getCashBox().getNumber() + "-" + event.getInvoice().getInvoiceNumber(),
                            authorization + verificationDigit,
                            client,
                            0,
                            subtotal.doubleValue(),
                            0,
                            event.getInvoice().getTaxAmount(),
                            event.getInvoice().getAmount(),
                            listProducts,
                            0,
                            "Generado en 4YourLife",
                            "P"
                    )
            );

            if (event.getInvoice().getInvoiceContifico().cliente.cedula.length() == 13) {
                event.getInvoice().getInvoiceContifico().cliente.ruc = event.getInvoice().getInvoiceContifico().cliente.cedula;
                String cedula = event.getInvoice().getInvoiceContifico().cliente.cedula;
                event.getInvoice().getInvoiceContifico().cliente.cedula = cedula.substring(0, cedula.length() - 3);
            }

            var invoice = commandInvoiceService.save(event.getInvoice());
            commandInvoiceService.sendInvoiceToContifico(invoice);
            System.out.println("Invoice created and saved successfully.");

        } catch (Exception e) {
            System.err.println("Error creating or saving invoice: " + e.getMessage());
            e.printStackTrace();
        }
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
}