package com.foryourlife.clients.account.invitations.domain;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository {
    void save(Invitation invitation);
    Optional<Invitation> findByToken(String token);
    List<Invitation> findBySenderId(String token);
    Optional<Invitation> findTopBySenderIdOrderByQuantityDesc(String id);
}
