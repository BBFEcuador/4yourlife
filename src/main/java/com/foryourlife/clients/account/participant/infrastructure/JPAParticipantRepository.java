package com.foryourlife.clients.account.participant.infrastructure;

import com.foryourlife.clients.account.participant.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPAParticipantRepository extends JpaRepository<Participant, String>, JpaSpecificationExecutor<Participant> {
    Optional<Participant> findByUser_email(String email);
    Optional<Participant> findByUser_Id(String userId);
    @Query("SELECT p FROM Participant p WHERE p.user.id IN :userIds")
    List<Participant> findAllByUserIds(@Param("userIds") List<String> userIds);

}
