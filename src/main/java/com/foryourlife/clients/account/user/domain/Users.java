package com.foryourlife.clients.account.user.domain;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.UserCreated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Users extends AggregateRoot {
    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "participant_level_id", referencedColumnName = "id")
    private ParticipantLevel participantLevel;

    protected Users() {
    }

    private Users(String id, String email, String password, String name, String phone, ParticipantLevel role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.participantLevel =role;
    }

    public static Users create(String id, String email, String password, String name, String phone, ParticipantLevel role) {
        var user = new Users(id, email, password, name, phone, role);
        user.record(new UserCreated(id, user));
        return user;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public ParticipantLevel getParticipantLevel() {
        return participantLevel;
    }

    public void setParticipantLevel(ParticipantLevel participantLevel) {
        this.participantLevel = participantLevel;
    }

    public String getRoleId(){
        return participantLevel.getId();
    }

}
