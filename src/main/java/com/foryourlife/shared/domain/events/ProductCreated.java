package com.foryourlife.shared.domain.events;

import com.foryourlife.admin.sales.product.domain.Product;
import com.foryourlife.shared.domain.bus.DomainEvent;

import java.io.Serializable;
import java.util.HashMap;

public class ProductCreated extends DomainEvent {
    private final Product product;

    public ProductCreated(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
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
