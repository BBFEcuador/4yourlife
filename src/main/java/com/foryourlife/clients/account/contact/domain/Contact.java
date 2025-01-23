package com.foryourlife.clients.account.contact.domain;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.user.domain.Users;
import jakarta.persistence.*;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    private String id;
    private String name;
    private String relationship;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    private Contact(String id, String name, String relationship, String phone, Users user) {
        this.id = id;
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
        this.user = user;
    }

    protected Contact() {
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

    public Users getUser() {
        return user;
    }

    public static Contact create(String id, String name, String relationship, String phone, Users user) {
        return new Contact(id, name, relationship, phone, user);
    }
}
