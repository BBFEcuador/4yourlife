package com.foryourlife.admin.sales.invoices.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.admin.contifico.config.domain.ConfigContificoRepository;
import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.invoices.domain.InvoiceContificoJson;
import com.foryourlife.admin.sales.invoices.domain.InvoiceRepository;
import com.foryourlife.admin.sales.invoices.infrastructure.http.InvoiceRequest;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InvoiceCommandService {
    private final InvoiceRepository invoiceRepository;
    private final ConfigContificoRepository configContificoRepository;
    @Qualifier("restClient")
    private final RestClient httpClient;

    public InvoiceCommandService(InvoiceRepository invoiceRepository, ConfigContificoRepository configContificoRepository, RestClient httpClient) {
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
        var configContifico = configContificoRepository
                .findByCampusId(invoice.getPayment().getCampus().getId())
                .orElseThrow(() -> new BaseException("Config for the campus not found", List.of("")));
        String formattedInvoiceDate = invoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        BigDecimal divisor = new BigDecimal("1.15");

        BigDecimal subtotal = invoice.getInvoiceContifico().total.divide(divisor, 2, RoundingMode.HALF_UP);

        BigDecimal recalculatedTotal = subtotal.multiply(divisor).setScale(2, RoundingMode.HALF_UP);

        if (recalculatedTotal.compareTo(invoice.getInvoiceContifico().total) != 0) {
            BigDecimal diff = invoice.getInvoiceContifico().total.subtract(recalculatedTotal);

            subtotal = subtotal.add(diff.divide(divisor, 2, RoundingMode.HALF_UP));
        }

//        BigDecimal taxAmount = invoice.getInvoiceContifico().total.subtract(subtotal);

        invoice.getInvoiceContifico().getDetalles().getFirst().setPrecio(subtotal);
        invoice.getInvoiceContifico().getDetalles().getFirst().setBase_gravable(subtotal);
        InvoiceContificoJson contificoJson = new InvoiceContificoJson(
                configContifico.getApiKey(),
                formattedInvoiceDate,
                "FAC",
                invoice.getInvoiceContifico().documento,
                "",
                invoice.getInvoiceContifico().cliente,
                BigDecimal.ZERO,
                invoice.getInvoiceContifico().subtotal_12,
                BigDecimal.ZERO,
                invoice.getInvoiceContifico().iva,
                invoice.getInvoiceContifico().total,
                invoice.getPayment().getNote(),
                invoice.getInvoiceContifico().detalles,
                BigDecimal.ZERO,
                "Generado en 4YourLife",
                "P",
                true,
                invoice.getInvoiceContifico().cobros
        );

        invoice.setInvoiceContifico(contificoJson);
        try {
            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(invoice.getInvoiceContifico());

            System.out.println("JSON enviado a Contifico:");
            System.out.println(json);

            ResponseEntity<String> response = httpClient.post()
                    .uri("https://api.contifico.com/sistema/api/v1/documento/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .header("Api-Token", configContifico.getApiKey())
                    .header("Authorization", configContifico.getApiSecret())
                    .retrieve()
                    .toEntity(String.class);

            var status = response.getStatusCode();
            System.out.println("Contifico response: " + status);

            if (status.is2xxSuccessful()) {
                if (status.is2xxSuccessful()) {
                    JsonNode rootNode = mapper.readTree(response.getBody());
                    String contificoId = rootNode.get("id").asText();

                    invoice.setSentContifico(true);
                    var newContificoJson = invoice.getInvoiceContifico();
                    newContificoJson.autorizacion = rootNode.get("autorizacion").asText();
                    invoice.setInvoiceContifico(newContificoJson);
                    invoice.setContificoId(contificoId);
                    System.out.println("Invoice sent to Contifico successfully. ID: " + contificoId);

                    invoice.setContificoError(null);
                    try {
                        String sriUrl = "https://api.contifico.com/sistema/api/v1/documento/"
                                + contificoId + "/sri/";

                        System.out.println("Enviando al SRI: " + sriUrl);

                        ResponseEntity<String> sriResponse = httpClient.put()
                                .uri(sriUrl)
                                .header("Api-Token", configContifico.getApiKey())
                                .header("Authorization", configContifico.getApiSecret())
                                .retrieve()
                                .toEntity(String.class);

                        System.out.println("SRI response: " + sriResponse.getStatusCode());

                        if (sriResponse.getStatusCode().is2xxSuccessful()) {
                            System.out.println("Factura enviada al SRI correctamente.");
                            invoice.setContificoError(null);
                        }
                    } catch (Exception e) {
                        System.err.println("Error enviando al SRI: " + e.getMessage());
                    }
                }
            }
            invoiceRepository.save(invoice);
        } catch (HttpClientErrorException e) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode errorNode = mapper.readTree(e.getResponseBodyAsString());
                String errorMessage = errorNode.has("mensaje")
                        ? errorNode.get("mensaje").asText()
                        : "Error desconocido";

                invoice.setContificoError(errorMessage);
                invoiceRepository.save(invoice);

            } catch (Exception jsonException) {
                invoice.setContificoError("Error al procesar respuesta de error: " + e.getMessage());
                invoiceRepository.save(invoice);
            }

            System.err.println("Error sending invoice to Contifico: " + e.getResponseBodyAsString());

        } catch (Exception e) {
            System.err.println("Error sending invoice to Contifico (second): " + e.getMessage());
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
