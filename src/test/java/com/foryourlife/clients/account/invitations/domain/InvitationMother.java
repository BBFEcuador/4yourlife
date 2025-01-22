package com.foryourlife.clients.account.invitations.domain;

import com.foryourlife.clients.account.user.domain.Users;
import com.foryourlife.clients.account.user.domain.UsersMother;

import java.util.UUID;

public class InvitationMother {

    public static Invitation random() {
        return Invitation.create(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UsersMother.random(),
                false
        );
    }
    public static Invitation random(Users user) {
        return Invitation.create(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                user,
                false
        );
    }
}