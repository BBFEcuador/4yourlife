package com.foryourlife.clients.account.invitations.domain;

import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.InvitationCreated;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "invitation")
public class Invitation extends AggregateRoot {
    @Id
    private String id;
    @Column(unique = true)
    private String token;
    @OneToOne(optional = true)
    @JoinColumn(referencedColumnName = "id", name = "user_id", nullable = true)
    private Participant users;
    private Boolean isAdmin;
    private Boolean isUsed;
    private String senderId;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Sender enrolled;

    private Invitation(String id, String token, Participant users, Boolean isAdmin, Boolean isUsed, String senderId, Sender enrolled) {
        this.id = id;
        this.token = token;
        this.users = users;
        this.isAdmin = isAdmin;
        this.isUsed = isUsed;
        this.senderId = senderId;
        this.enrolled = enrolled;
    }


    protected Invitation() {
    }


    public static Invitation create(String id, String token, Participant users, Boolean isAdmin, String senderId, Sender enrolled) {
        var invitation = new Invitation(id, token, users, isAdmin, false, senderId, enrolled);
        invitation.record(new InvitationCreated(id, invitation));
        return invitation;
    }


    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Participant getUsers() {
        return users;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public String getSenderId() {
        return senderId;
    }

    public Sender getEnrolled() {
        return enrolled;
    }

    public void consumeToken(){
        this.isUsed = true;
    }

    public void setUsers(Participant users) {
        this.users = users;
    }
}
