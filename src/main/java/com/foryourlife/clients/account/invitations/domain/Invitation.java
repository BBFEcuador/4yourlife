package com.foryourlife.clients.account.invitations.domain;

import com.foryourlife.clients.account.user.domain.Users;
import com.foryourlife.shared.domain.AggregateRoot;
import com.foryourlife.shared.domain.events.InvitationCreated;
import jakarta.persistence.*;

@Entity
@Table(name = "invitation")
public class Invitation extends AggregateRoot {
    @Id
    private String id;
    @Column(unique = true)
    private String token;
    @OneToOne(optional = true)
    @JoinColumn(referencedColumnName = "id", name = "user_id",nullable = true)
    private Users users;
    private Boolean isAdmin;
    private Boolean isUsed;

    public Invitation(String id, String token, Users users, Boolean isAdmin, Boolean isUsed) {
        this.id = id;
        this.token = token;
        this.users = users;
        this.isAdmin = isAdmin;
        this.isUsed = isUsed;
    }


    protected Invitation() {
    }


    public static Invitation create(String id, String token, Users users, Boolean isAdmin) {
        var invitation = new Invitation(id,token,users,isAdmin,false);
        invitation.record(new InvitationCreated(id,invitation));
        return invitation;
    }


    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Users getUsers() {
        return users;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public Boolean getUsed() {
        return isUsed;
    }
}
