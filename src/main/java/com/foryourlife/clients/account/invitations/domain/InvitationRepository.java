package com.foryourlife.clients.account.invitations.domain;

import java.util.Optional;

public interface InvitationRepository {
    void save(Invitation invitation);
    Optional<Invitation> findByToken(String token);
}
