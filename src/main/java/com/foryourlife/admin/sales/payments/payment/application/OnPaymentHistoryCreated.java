package com.foryourlife.admin.sales.payments.payment.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.foryourlife.admin.contifico.config.application.ConfigContificoQueryService;
import com.foryourlife.shared.domain.bus.DomainEventSubscriber;
import com.foryourlife.shared.domain.events.PaymentHistoryCreated;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.format.DateTimeFormatter;

@Service
@DomainEventSubscriber(value = PaymentHistoryCreated.class)
public class OnPaymentHistoryCreated {
    private final RestClient httpClient;
    private final ConfigContificoQueryService configContificoQueryService;
    private final ObjectMapper objectMapper;


    public OnPaymentHistoryCreated(RestClient httpClient, ConfigContificoQueryService configContificoQueryService, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.configContificoQueryService = configContificoQueryService;
        this.objectMapper = objectMapper;
    }

    @EventListener
    public void handle(PaymentHistoryCreated event) {
        try{
            ObjectNode jsonNode = objectMapper.createObjectNode();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = event.getInvoice().getInvoiceDate().format(formatter);


            switch (event.getPaymentHistory().getPaymentMethod().getCode()){
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

            var paymentHistoryJson = new ObjectMapper().writeValueAsString(jsonNode);

            ResponseEntity<String> response = httpClient.post()
                    .uri("https://api.contifico.com/sistema/api/v1/documento/"+ event.getInvoice().getContificoId() +"/cobro/")
                    .body(paymentHistoryJson)
                    .header("Api-Token", configContifico.getApiKey())
                    .header("Authorization", configContifico.getApiSecret())
                    .retrieve()
                    .toEntity(String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Payment created in Contifico: " + response.getBody());
            }else {
                System.err.println("Error creating payment in Contifico: " + response.getStatusCodeValue() + ", " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Error creating payment in Contifico: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
