package com.foryourlife.shared.domain.events;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;

public class PaymentHistoryCreated extends DomainEvent {
    private final PaymentHistory paymentHistory;
    private final Invoice invoice;

    public PaymentHistoryCreated(PaymentHistory paymentHistory, Invoice invoice) {
        this.paymentHistory = paymentHistory;
        this.invoice = invoice;
    }

    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    @Override
    public String eventName() {
        return "";
    }

    @Override
    public HashMap<String, Serializable> toPrimitives() {
        return null;
    }

    @Override
    public DomainEvent fromPrimitives(String aggregateId, HashMap<String, Serializable> body, String eventId, String occurredOn) {
        return null;
    }
}
