package com.foryourlife.admin.sales.payments.payment.application;

import com.foryourlife.admin.sales.invoices.application.CommandInvoiceService;
import com.foryourlife.admin.sales.invoices.application.QueryInvoiceService;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerDetail;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawerRepository;
import com.foryourlife.admin.sales.product.application.ProductFinderService;
import com.foryourlife.clients.account.module.application.ClientModuleCreatorService;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.clients.account.participant.domain.ParticipantRepository;
import com.foryourlife.clients.account.participantLevel.application.ParticipantLevelService;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.PaymentCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public OnPaymentCreated(ProductFinderService productFinderService, ClientModuleCreatorService clientModuleCreatorService, CommandInvoiceService commandInvoiceService, QueryInvoiceService queryInvoiceService, ParticipantRepository participantRepository, ParticipantLevelService participantLevelRepository) {
        this.productFinderService = productFinderService;
        this.clientModuleCreatorService = clientModuleCreatorService;
        this.commandInvoiceService = commandInvoiceService;
        this.queryInvoiceService = queryInvoiceService;
        this.participantRepository = participantRepository;
        this.participantLevelRepository = participantLevelRepository;
    }

    @Async
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

        createInvoice(event);
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

            int invoiceNumber = (lastInvoice != null)
                    ? Integer.parseInt(lastInvoice.getInvoiceNumber()) + 1
                    : 1;
            event.getInvoice().setInvoiceNumber(String.valueOf(invoiceNumber));
            event.getInvoice().setId(UUID.randomUUID().toString());
            commandInvoiceService.save(event.getInvoice());
            System.out.println("Invoice created and saved successfully.");
        } catch (Exception e) {
            System.err.println("Error creating or saving invoice: " + e.getMessage());
            e.printStackTrace();
        }
    }
}