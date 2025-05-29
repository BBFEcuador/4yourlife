package com.foryourlife.shared.domain.events;

import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;

public class PaymentCreated extends DomainEvent {
    private final Payment payment;
    private final DataInvoice dataInvoice;

    public PaymentCreated(Payment payment, DataInvoice dataInvoice) {
        this.payment = payment;
        this.dataInvoice = dataInvoice;
    }

    public Payment getPayment() {
        return payment;
    }

    public DataInvoice getDataInvoice() {
        return dataInvoice;
    }

    @Override
    public String eventName() {
        return "payment_created";
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
