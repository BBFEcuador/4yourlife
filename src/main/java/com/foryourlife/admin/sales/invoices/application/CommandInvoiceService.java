package com.foryourlife.admin.sales.invoices.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceContificoJson;
import com.foryourlife.admin.sales.invoices.domain.InvoiceRepository;
import com.foryourlife.admin.sales.invoices.infrastructure.http.InvoiceRequest;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.events.PaymentHistoryCreated;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CommandInvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ConfigContificoRepository configContificoRepository;
    @Qualifier("restClient")
    private final RestClient httpClient;
    private final EventBus eventBus;

    public CommandInvoiceService(InvoiceRepository invoiceRepository, ConfigContificoRepository configContificoRepository, RestClient httpClient, EventBus eventBus) {
        this.invoiceRepository = invoiceRepository;
        this.configContificoRepository = configContificoRepository;
        this.httpClient = httpClient;
        this.eventBus = eventBus;
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public void update(InvoiceRequest invoiceReq) {
        if (invoiceReq.id == null) {
            throw new IllegalArgumentException("No se puede actualizar, ID nula");
        }

        var invoice = invoiceRepository.findById(invoiceReq.id);

        if (invoice.getSentContifico() == true)
            throw new IllegalArgumentException("No se puede actualizar, ya fue enviada al SRI");
        invoice.setFullName(invoiceReq.fullName);
        invoice.setAddress(invoiceReq.address);
        invoice.setDocument(invoiceReq.document);
        invoice.setPhone(invoiceReq.phone);
        invoice.setEmail(invoiceReq.email);

        invoice.getInvoiceContifico().setCliente(new InvoiceContificoJson.Cliente(invoiceReq.document, invoiceReq.fullName, invoiceReq.phone, invoiceReq.address, invoiceReq.type, invoiceReq.email));

        invoiceRepository.save(invoice);
        sendInvoiceToContifico((invoice));
    }

    public void resendToContifico(String campusId) {
        var invoices = List.<Invoice>of();
        if (campusId != null && !campusId.isEmpty()) {
            invoices = invoiceRepository.findInvoicesBySentContificoAndCampusId(false, campusId);
        } else {
            invoices = invoiceRepository.findInvoicesBySentContifico(false);
        }
        for (Invoice invoice : invoices) {
            sendInvoiceToContifico(invoice);
        }
    }

    public void sendInvoiceToContifico(Invoice invoice) {
        var configContifico = configContificoRepository.findByCampusId(invoice.getPayment().getCampus().getId()).orElseThrow(() -> new BaseException("Config for the campus not found", List.of("")));
        try {
            var json = new ObjectMapper().writeValueAsString(invoice.getInvoiceContifico());
            ResponseEntity<String> response = httpClient.post().uri("https://api.contifico.com/sistema/api/v1/documento/").body(json).header("Api-Token", configContifico.getApiKey()).header("Authorization", configContifico.getApiSecret()).retrieve().toEntity(String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            var status = response.getStatusCode();
            System.out.println(status);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                invoice.setSentContifico(true);
                invoice.setContificoId(rootNode.get("id").asText());
                System.out.println("Invoice sent to contifico successfully.");
            }
            invoiceRepository.save(invoice);

            invoice.getPayment().getPaymentshistory().forEach(history -> {
                if (!history.getSent()){
                    PaymentHistoryCreated event = new PaymentHistoryCreated(history, invoice);
                    eventBus.publish(List.of(event));
                }
            });

        } catch (HttpClientErrorException e) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode errorNode = objectMapper.readTree(e.getResponseBodyAsString());
                String errorMessage = errorNode.has("mensaje") ? errorNode.get("mensaje").asText() : "Error desconocido";
                invoice.setContificoError(errorMessage);
                invoiceRepository.save(invoice);
            } catch (Exception jsonException) {
                invoice.setContificoError("Error al procesar la respuesta de error: " + e.getMessage());
                invoiceRepository.save(invoice);
            }
            System.err.println("Error sending invoice to contifico: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Error sending invoice to contifico second: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void resendPaymentHistoryToContifico(String paymentId) {
//        var payment = paymentRepository.findById(paymentId);
//        var invoice = invoiceRepository.findByPaymentId(paymentId);
//        if (invoice == null) {
//            throw new IllegalArgumentException("No se encontró la factura para el pago con ID: " + paymentId);
//        }
//        payment.getPaymentshistory().forEach(history -> {
//            if (!history.getSent()){
//                PaymentHistoryCreated event = new PaymentHistoryCreated(history, invoice);
//                eventBus.publish(List.of(event));
//            }
//        });
    }
}
