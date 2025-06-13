package com.foryourlife.admin.sales.payments.payment.infrastructure.httpControllers;

import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.invoices.infrastructure.http.InvoiceRequest;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PaymentRequest {
    public String id;

    @NotNull(message = "Los productos son requeridos")
    public List<String> products;
    public ProductDiscount discount;
    @NotNull(message = "El Participante es requerido")
    public String participant;
    @NotNull(message = "El campus es requerido")
    public String campus;
    @NotNull(message = "Pago es requerido")
    public List<PaymentHistory> paymentshistory = List.of();
    @NotNull(message = "El valor total es requerido")
    public Double total;
    public PaymentStatus status;
    @NotNull(message = "Los datos de facturación son requeridos")
    public InvoiceRequest invoice;
    public String note;
    public String cashDrawerId;
    public String userId;
}