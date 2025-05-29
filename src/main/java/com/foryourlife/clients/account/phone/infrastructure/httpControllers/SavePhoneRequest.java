package com.foryourlife.clients.account.phone.infrastructure.httpControllers;

import com.foryourlife.clients.account.phone.domain.Phone;
import com.foryourlife.clients.account.participant.domain.Participant;

public class SavePhoneRequest {
    private String id;
    private String phone;
    private Participant user;

    public SavePhoneRequest(String id, String phone, Participant user) {
        this.id = id;
        this.phone = phone;
        this.user = user;
    }

    public Phone toDomain() {
        return Phone.create(id != null ? id: java.util.UUID.randomUUID().toString(), phone, user);
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public Participant getUser() {
        return user;
    }
}
