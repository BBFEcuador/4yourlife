package com.foryourlife.admin.sales.payments.payment.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.foryourlife.admin.contifico.config.application.ConfigContificoQueryService;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentRepository;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.PaymentHistoryCreated;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@DomainEventSubscriber(value = PaymentHistoryCreated.class)
public class OnPaymentHistoryCreated {
    private final RestClient httpClient;
    private final ConfigContificoQueryService configContificoQueryService;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;

    public OnPaymentHistoryCreated(RestClient httpClient, ConfigContificoQueryService configContificoQueryService, ObjectMapper objectMapper, PaymentRepository paymentRepository) {
        this.httpClient = httpClient;
        this.configContificoQueryService = configContificoQueryService;
        this.objectMapper = objectMapper;
        this.paymentRepository = paymentRepository;
    }

    @EventListener
    public void handle(PaymentHistoryCreated event) {
        try {
            ObjectNode jsonNode = objectMapper.createObjectNode();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = event.getInvoice().getInvoiceDate().format(formatter);


            switch (event.getPaymentHistory().getPaymentMethod().getCode()) {
                case "EF":
                    jsonNode.put("forma_cobro", "EF");
                    jsonNode.put("monto", event.getPaymentHistory().getAmount());
                    jsonNode.put("fecha", formattedDate);
                    break;
                case "CQ":
                    jsonNode.put("forma_cobro", "CQ");
                    jsonNode.put("monto", event.getPaymentHistory().getAmount());
                    jsonNode.put("fecha", formattedDate);
                    jsonNode.put("numero_cheque", event.getPaymentHistory().getTransactionId());
                    break;
                case "TC":
                    jsonNode.put("forma_cobro", "TC");
                    jsonNode.put("monto", event.getPaymentHistory().getAmount());
                    jsonNode.put("fecha", formattedDate);
                    jsonNode.put("tipo_ping", event.getPaymentHistory().getPingType() != null ? event.getPaymentHistory().getPingType() : "D");
                    break;
                case "TRA":
                    jsonNode.put("forma_cobro", "TRA");
                    jsonNode.put("monto", event.getPaymentHistory().getAmount());
                    jsonNode.put("cuenta_bancaria_id", event.getPaymentHistory().getPaymentMethod().getBank().getContificoId());
                    jsonNode.put("numero_comprobante", event.getPaymentHistory().getTransactionId());
                    jsonNode.put("fecha", formattedDate);
                    break;
                default:
                    throw new RuntimeException("Invalid payment method");
            }

            var configContifico = configContificoQueryService.findConfigContificoByCampusId(event.getInvoice().getPayment().getCampus().getId());

            var paymentHistoryJson = objectMapper.writeValueAsString(jsonNode);

            ResponseEntity<String> response = httpClient.post()
                    .uri("https://api.contifico.com/sistema/api/v1/documento/" + event.getInvoice().getContificoId() + "/cobro/")
                    .body(paymentHistoryJson)
                    .header("Api-Token", configContifico.getApiKey())
                    .header("Authorization", configContifico.getApiSecret())
                    .retrieve()
                    .toEntity(String.class);
            boolean sentOk = response.getStatusCode().is2xxSuccessful();

            if (sentOk) {
                System.out.println("Payment created in Contifico: " + response.getBody());
            } else {
                System.err.println("Error creating payment in Contifico: "
                        + response.getStatusCodeValue() + ", " + response.getBody());
            }

            Payment payment = paymentRepository.findById(event.getInvoice().getPayment().getId());

            if (payment.getPaymentshistory() != null) {
                payment.getPaymentshistory().stream()
                        .filter(ph -> ph.getId().equals(event.getPaymentHistory().getId()))
                        .findFirst()
                        .ifPresent(ph -> ph.setSent(sentOk));
            }

            paymentRepository.save(payment);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            System.err.println("Error serializing payment history JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (HttpClientErrorException e) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode errorNode = objectMapper.readTree(e.getResponseBodyAsString());
                String errorMessage = errorNode.has("mensaje") ? errorNode.get("mensaje").asText() : "Error desconocido";
                System.err.println("Error al crear el pago en contifico: " + e.getMessage());
                e.printStackTrace();
                Payment payment = paymentRepository.findById(event.getInvoice().getPayment().getId());

                if (payment.getPaymentshistory() != null) {
                    payment.getPaymentshistory().stream()
                            .filter(ph -> ph.getId().equals(event.getPaymentHistory().getId()))
                            .findFirst()
                            .ifPresent(ph -> ph.setSent(false));
                    payment.getPaymentshistory().stream()
                            .filter(ph -> ph.getId().equals(event.getPaymentHistory().getId()))
                            .findFirst()
                            .ifPresent(ph -> ph.setNotSendError(errorMessage));
                }

                paymentRepository.save(payment);
            } catch (Exception ex) {
                System.err.println("Error sending payment to contifico second: " + ex.getMessage());
                ex.printStackTrace();
            }

        }
    }
}
