package com.foryourlife.admin.sales.invoices.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceRepository;
import com.foryourlife.admin.sales.invoices.infrastructure.http.InvoiceRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
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

    public CommandInvoiceService(InvoiceRepository invoiceRepository, ConfigContificoRepository configContificoRepository, @Qualifier("restClient") RestClient httpClient) {
        this.invoiceRepository = invoiceRepository;
        this.configContificoRepository = configContificoRepository;
        this.httpClient = httpClient;
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
        invoice = Invoice.create(invoiceReq.id, invoiceReq.fullName, invoiceReq.address, invoiceReq.document, invoiceReq.phone, invoiceReq.email, invoice.getInvoiceNumber(), invoice.getInvoiceDate(), invoice.getProducts(), invoice.getPayment(), invoice.getSentContifico(), invoice.getTaxAmount(), invoice.getTax(), invoice.getAmount());
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

        } catch (HttpClientErrorException e) {
            try {
                if (e.getStatusCode() == HttpStatusCode.valueOf(400)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode errorNode = objectMapper.readTree(e.getResponseBodyAsString());
                    String errorMessage = errorNode.has("mensaje") ? errorNode.get("mensaje").asText() : "Error desconocido";
                    invoice.setContificoError(errorMessage);

                } else {
                    invoice.setContificoError("Error de cliente HTTP: " + e.getStatusCode());
                }
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
}
