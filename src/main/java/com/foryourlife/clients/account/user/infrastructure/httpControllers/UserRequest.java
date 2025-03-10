package com.foryourlife.clients.account.user.infrastructure.httpControllers;

import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevel;
import com.foryourlife.clients.account.profileDetails.domain.ProfileDetails;
import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class UserRequest {

    @Null
    private String id;
    @NotNull
    private User user;
    @NotNull
    private ParticipantLevel participantLevel;
    @NotNull
    private ProfileDetails profile;
    @NotNull
    private String invitationToken;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ParticipantLevel getParticipantLevel() {
        return participantLevel;
    }

    public ProfileDetails getProfile() {
        return profile;
    }

    public String getInvitationToken() {
        return invitationToken;
    }

    public Participant toDomain() {
        return Participant.create(id, user, participantLevel, profile, invitationToken);
    }
}
