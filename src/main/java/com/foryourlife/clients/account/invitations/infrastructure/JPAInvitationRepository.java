package com.foryourlife.clients.account.invitations.infrastructure;

import com.foryourlife.clients.account.invitations.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPAInvitationRepository extends JpaRepository<Invitation, String> {
    Optional<Invitation> findByToken(String token);
}
