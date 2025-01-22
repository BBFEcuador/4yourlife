package com.foryourlife.clients.account.invitations.infrastructure;

import com.foryourlife.clients.account.invitations.domain.InvitationMother;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InvitationRepositoryImplTest {

    @Autowired
    private InvitationRepository repository;

    @Test
    void create_a_valid_invitation() {
        var invitation = InvitationMother.random(null);
        repository.save(invitation);
        Assertions.assertTrue(repository.findByToken(invitation.getToken()).isPresent());
    }

    @Test
    void findByToken() {
    }
}