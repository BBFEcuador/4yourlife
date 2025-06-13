package com.foryourlife.shared.domain.events;

import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.admin.sales.payments.payment.domain.PaymentHistory;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;

public class PaymentHistoryCreated extends DomainEvent {
    private final PaymentHistory PaymentHistory;
    private final Payment payment;
    private final String cashDrawerId;

    public PaymentHistoryCreated(PaymentHistory paymentHistory, Payment payment, String cashDrawerId) {
        PaymentHistory = paymentHistory;
        this.payment = payment;
        this.cashDrawerId = cashDrawerId;
    }

    public PaymentHistory getPaymentHistory() {
        return PaymentHistory;
    }

    public Payment getPayment() {
        return payment;
    }

    public String getCashDrawerId() {
        return cashDrawerId;
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
