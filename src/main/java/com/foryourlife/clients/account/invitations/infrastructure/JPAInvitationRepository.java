package com.foryourlife.clients.account.invitations.infrastructure;

import com.foryourlife.clients.account.invitations.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface JPAInvitationRepository extends JpaRepository<Invitation, String>, JpaSpecificationExecutor<Invitation> {
    Optional<Invitation> findByToken(String token);

    List<Invitation> findBySenderId(String token);

    @Query("SELECT i FROM Invitation i WHERE i.senderId = :senderId AND i.quantity > 0 ORDER BY i.quantity DESC LIMIT 1")
    List<Invitation> findTopBySenderIdOrderByQuantityDesc(@Param("senderId") String senderId);

    List<Invitation> findAllByTokenIn(Collection<String> tokens);

    List<Invitation> findAllBySenderIdOrderByQuantityDesc(String senderId);
}
