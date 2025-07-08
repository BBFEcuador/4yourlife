package com.foryourlife.shared.domain.events;

import com.foryourlife.admin.sales.invoices.domain.Invoice;
import com.foryourlife.admin.sales.payments.cashDrawer.domain.CashDrawer;
import com.foryourlife.admin.sales.payments.payment.domain.Payment;
import com.foryourlife.clients.account.invoiceData.domain.DataInvoice;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;

public class PaymentCreated extends DomainEvent {
    private final Payment payment;
    private final Invoice invoice;
    private final CashDrawer cashDrawer;

    public PaymentCreated(Payment payment, Invoice invoice, CashDrawer cashDrawer) {
        this.payment = payment;
        this.invoice = invoice;
        this.cashDrawer = cashDrawer;
    }

    public CashDrawer getCashDrawer() {
        return cashDrawer;
    }

    public Payment getPayment() {
        return payment;
    }

    public Invoice getInvoice() {
        return invoice;
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
