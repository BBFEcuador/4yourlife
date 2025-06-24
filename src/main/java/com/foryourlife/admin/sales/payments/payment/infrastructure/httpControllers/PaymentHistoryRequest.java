package com.foryourlife.admin.sales.payments.payment.infrastructure.httpControllers;

import com.foryourlife.admin.sales.invoices.infrastructure.http.InvoiceRequest;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentHistoryRequest {
    @NotNull(message = "Payment History is required")
    public PaymentHistory paymentHistory;
    public InvoiceRequest invoice;
    @NotNull(message = "Invoice is required")
    @NotBlank(message = "Invoice ID is required")
    public String cashDrawerId;
}
