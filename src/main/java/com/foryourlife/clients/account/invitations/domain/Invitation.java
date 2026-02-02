package com.foryourlife.clients.account.invitations.domain;

import com.foryourlife.admin.programs.campus.domain.Campus;
import com.foryourlife.clients.account.participant.domain.Participant;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.InvitationCreated;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invitation")
public class Invitation extends AggregateRoot implements Serializable {
    @Id
    private String id;
    @Column(unique = true)
    private String token;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<EnrolledUsers> users = List.of();
    @ManyToOne()
    @JoinColumn(referencedColumnName = "id", name = "campus_id")
    private Campus campus;
    private Boolean isAdmin;
    private Boolean isUsed;
    private String senderId;
    private Integer quantity;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Sender enrolled;

    private Invitation(String id, String token, Boolean isAdmin, Boolean isUsed, String senderId, Sender enrolled, Integer quantity, Campus campus) {
        this.id = id;
        this.token = token;
        this.isAdmin = isAdmin;
        this.isUsed = isUsed;
        this.senderId = senderId;
        this.quantity = quantity;
        this.enrolled = enrolled;
        this.campus = campus;
    }


    protected Invitation() {
    }


    public static Invitation create(String id, String token, Participant users, Boolean isAdmin, String senderId, Sender enrolled, Integer quantity, Campus campus) {
        var invitation = new Invitation(id, token, isAdmin, false, senderId, enrolled, quantity, campus);
        invitation.record(new InvitationCreated(id, invitation));
        return invitation;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public List<EnrolledUsers> getUsers() {
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

    public void consumeToken() {
        if (this.quantity == 0) return;
        this.quantity--;
        if (this.quantity == 0) this.isUsed = true;
    }

    public void setUsers(List<EnrolledUsers> users) {
        this.users = users;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }


}
