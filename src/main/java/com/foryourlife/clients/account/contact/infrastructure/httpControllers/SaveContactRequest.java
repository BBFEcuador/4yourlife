package com.foryourlife.clients.account.contact.infrastructure.httpControllers;

import com.foryourlife.clients.account.contact.domain.Contact;
import com.foryourlife.clients.account.user.domain.Users;

import java.util.UUID;

public class SaveContactRequest {
    private String id;
    private String name;
    private String relationship;
    private String phone;
    private Users userId;

    public SaveContactRequest(String id, String name, String relationship, String phone, Users userId) {
        this.id = id;
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
        this.userId = userId;
    }

    public Contact toDomain() {
        return Contact.create(id != null ? id: UUID.randomUUID().toString(), name, relationship, phone, userId);
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

    public Users getUserId() {
        return userId;
    }
}
