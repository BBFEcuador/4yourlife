package com.foryourlife.clients.account.phone.infrastructure.httpControllers;

import com.foryourlife.clients.account.phone.domain.Phone;
import com.foryourlife.clients.account.user.domain.Users;

public class SavePhoneRequest {
    private String id;
    private String phone;
    private Users user;

    public SavePhoneRequest(String id, String phone, Users user) {
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

    public Users getUser() {
        return user;
    }
}
