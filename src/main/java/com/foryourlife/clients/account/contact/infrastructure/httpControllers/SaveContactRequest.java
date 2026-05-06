package com.foryourlife.clients.account.contact.infrastructure.httpControllers;

import com.foryourlife.clients.account.contact.domain.Contact;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class SaveContactRequest {
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String relationship;
    @NotNull
    private String phone;
    private String userId;

    public SaveContactRequest(String id, String name, String relationship, String phone, String userId) {
        this.id = id;
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
        this.userId = userId;
    }

    public Contact toDomain() {
        return Contact.create(id != null ? id : UUID.randomUUID().toString(), name, relationship, phone, null);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserId() {
        return userId;
    }
}
