package com.foryourlife.clients.account.invitations.applications;

import com.foryourlife.clients.account.invitations.domain.InvitationMother;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.shared.domain.UnitTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CommandInvitationServiceTest extends UnitTestCase {

    private InvitationRepository invitationRepository;

    @BeforeEach
    public void setup() {
        invitationRepository = Mockito.mock(InvitationRepository.class);
    }

    @Test
    void create_a_valid_invitation_by_user() {
        var invitation = InvitationMother.random();
        invitationRepository.save(invitation);
        Mockito.verify(invitationRepository,Mockito.atLeastOnce()).save(invitation);
    }

    @Test
    void createInvitationByAdmin() {
    }
}