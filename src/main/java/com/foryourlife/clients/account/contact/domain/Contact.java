package com.foryourlife.clients.account.contact.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.infrastructure.auditable.AuditableEntity;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "contacts")
public class Contact extends AuditableEntity implements Serializable {

    @Id
    private String id;
    private String name;
    private String relationship;
    private String phone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Participant user;

    private Contact(String id, String name, String relationship, String phone, Participant user) {
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
    @JsonIgnore
    public Participant getUser() {
        return user;
    }

    public static Contact create(String id, String name, String relationship, String phone, Participant user) {
        return new Contact(id, name, relationship, phone, user);
    }

    public void setUser(Participant user) {
        this.user = user;
    }
}
