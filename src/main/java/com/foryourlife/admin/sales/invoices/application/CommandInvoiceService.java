package com.foryourlife.admin.sales.invoices.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceRepository;
import com.foryourlife.admin.sales.invoices.infrastructure.http.InvoiceRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
        if (invoiceRepository.findById(invoiceReq.id).getSentContifico() == true)
            throw new IllegalArgumentException("No se puede actualizar, ya fue enviada al SRI");
        var invoice = Invoice.create(
                invoiceReq.id,
                invoiceReq.fullName,
                invoiceReq.address,
                invoiceReq.document,
                invoiceReq.phone,
                invoiceReq.email,
                invoiceReq.invoiceNumber,
                invoiceReq.invoiceDate,
                invoiceReq.products,
                invoiceReq.payment,
                invoiceReq.sentSri,
                invoiceReq.amount,
                15.0,
                invoiceReq.amount


        );
        invoiceRepository.save(invoice);
    }

    public void sendInvoiceToContifico(Invoice invoice) {
        var configContifico = configContificoRepository.findByCampusId(invoice.getPayment().getCampus().getId()).orElseThrow(
                () -> new BaseException("Config for the campus not found", List.of(""))
        );
        try {
            var json = new ObjectMapper().writeValueAsString(invoice.getInvoiceContifico());
            ResponseEntity<String> response = httpClient.post()
                    .uri("https://api.contifico.com/sistema/api/v1/documento/")
                    .body(json)
                    .header("Api-Token", configContifico.getApiKey())
                    .header("Authorization", configContifico.getApiSecret())
                    .retrieve()
                    .toEntity(String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                invoice.setSentContifico(true);
                invoice.setContificoId(rootNode.get("id").asText());
                invoiceRepository.save(invoice);
                System.out.println("Invoice sent to contifico successfully.");
            } else {
                System.err.println("Error sending invoice to contifico: " + response.getBody());
            }

        } catch (Exception e) {
            System.err.println("Error sending invoice to contifico: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
