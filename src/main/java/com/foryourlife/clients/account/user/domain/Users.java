package com.foryourlife.clients.account.user.domain;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.UserCreated;
import jakarta.persistence.*;

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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id",name = "profile_id")
    private ProfileDetails profile;

    protected Users() {
    }

    private Users(String id, String email, String password, String name, String phone, ParticipantLevel role,ProfileDetails profile) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.participantLevel =role;
        this.profile =profile;
    }

    public static Users create(String id, String email, String password, String name, String phone, ParticipantLevel role,ProfileDetails profile) {
        var user = new Users(id, email, password, name, phone, role,profile);
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
