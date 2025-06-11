package com.foryourlife.admin.sales.payments.payment.infrastructure.httpControllers;

import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import jakarta.validation.constraints.NotNull;

public class PaymentHistoryRequest {
    @NotNull(message = "El pago no puede estar vacío")
    public PaymentHistory paymentHistory;

    public String paymentMethod;
    public double amount;
    public String date;
    public String paymentMethodId;
}
