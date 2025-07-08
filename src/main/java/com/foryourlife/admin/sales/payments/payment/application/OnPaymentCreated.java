package com.foryourlife.admin.sales.payments.payment.application;

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
            int invoiceNumber = 0;
            if (lastInvoice == null) {
                 invoiceNumber = event.getCashDrawer().getCashBox().getFirstNumberInvoice();
            }else {
                 invoiceNumber = Integer.parseInt(lastInvoice.getInvoiceNumber()) + 1;
            }

            event.getInvoice().setInvoiceNumber(String.valueOf(invoiceNumber));
            event.getInvoice().setId(UUID.randomUUID().toString());

//            var authorization = event.getInvoice().getInvoiceDate().toString() + "01" + event.j;
//             event.getInvoice().setInvoiceContifico(
//                    new InvoiceContificoJson(
//                            "1234356789",
//                            event.getPayment().getCreated_at(),
//                            "FA",
//                            event.getInvoice().getDocument(),
//
//                    )
//            );
            commandInvoiceService.save(event.getInvoice());
            System.out.println("Invoice created and saved successfully.");

        } catch (Exception e) {
            System.err.println("Error creating or saving invoice: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendInvoiceToContifico(PaymentCreated event){


    }

        public static int generateModule(String claveAcceso) {
            int factor = 2;
            int suma = 0;
            for (int i = claveAcceso.length() - 1; i >= 0; i--) {
                suma += factor * Character.getNumericValue(claveAcceso.charAt(i));
                factor = factor % 7 == 0 ? 2 : factor + 1;
            }
            int dv = 11 - suma % 11;
            int ds = dv == 11 ? 0 : (dv == 10 ? 1 : dv);
            return ds;
        }

    // pos * la api token
    // fecha_emision
    // tipo_documento * FAC
    // documento
    // autorizacion * El numero de 49 digitos
    // cliente {
    //        "ruc": "0922054366001", required
    //        "cedula": "0922054366",required
    //        "razon_social": "Nombres del Cliente", required
    //        "telefonos": "0988800001",
    //        "direccion": "Direccion cliente",
    //        "tipo": "N", required
    //        "email": "cliente@contifico.com",
    //        "es_extranjero": false
    // }
    // subtotal_0 * 2 decimales
    // subtotal_15 * 2 decimales
    // ice * 2 decimales
    // iva * 2 decimales
    // total * 2 decimales
    //     "detalles": [{
    //        "producto_id": "RZxg87rxLh9Mb1pV", required
    //        "cantidad": 1.00, required
    //        "precio": 1.00, required
    //        "porcentaje_iva": 12,
    //        "porcentaje_descuento": 0.00, required
    //        "base_cero": 0.00, required
    //        "base_gravable": 1.00,
    //        "base_no_gravable": 0.00, required
    //        "porcentaje_ice": 15,
    //        "valor_ice" : null
    //    },
    // base_no_gravable
    // "cobros":[{
    //      "forma_cobro" : "TC", optional
    //      "monto" : 1.51,
    //      "numero_cheque" : "4567897",
    //      "tipo_ping" : "D"
    //    }]
    // monto required
    // tipo_ping *cuando sea la forma de pago TC
}