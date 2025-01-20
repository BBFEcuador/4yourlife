package com.foryourlife.payments.tenant.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Tenant {
    @Id
    private String id;
    private String document;
    private String ownerId;
    private boolean isActive;

    protected Tenant() {
    }

    private Tenant(String id, String document, String ownerId, boolean isActive) {
        this.id = id;
        this.document = document;
        this.ownerId = ownerId;
        this.isActive = isActive;
    }

    public static Tenant create(String id, String document, String ownerId, boolean isActive) {
        var tenant = new Tenant(id, document, ownerId, isActive);
        return tenant;
    }

    public String getId() {
        return id;
    }

    public String getDocument() {
        return document;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public boolean isActive() {
        return isActive;
    }
}
