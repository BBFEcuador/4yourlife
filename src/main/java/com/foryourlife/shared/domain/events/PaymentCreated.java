package com.foryourlife.shared.domain.events;

import com.foryourlife.payments.payment.domain.Payment;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class PaymentCreated extends DomainEvent {

    private final Payment payment;

    public PaymentCreated(String aggregateId, Payment payment) {
        super(aggregateId, UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }

    @Override
    public String eventName() {
        return "payment.payment.created";
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
