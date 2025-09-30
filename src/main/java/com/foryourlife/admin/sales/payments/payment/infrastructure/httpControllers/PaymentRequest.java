package com.foryourlife.admin.sales.payments.payment.infrastructure.httpControllers;

import com.foryourlife.admin.sales.discounts.domain.ProductDiscount;
import com.foryourlife.admin.sales.invoices.infrastructure.http.InvoiceRequest;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
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
    @NotEmpty(message = "Debe existir al menos un historial de pago")
    @Valid
    public List<PaymentHistory> paymentsHistory;
    @NotNull(message = "El valor total es requerido")
    public BigDecimal total;
    public BigDecimal totalDiscount;
    public PaymentStatus status;
    @NotNull(message = "Los datos de facturación son requeridos")
    @Valid
    public InvoiceRequest invoice;
    public String note;
    public String cashDrawerId;
    public String userId;
}