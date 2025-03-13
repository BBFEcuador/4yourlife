package com.foryourlife.clients.account.user.infrastructure;

import com.foryourlife.clients.account.user.domain.Participant;
import com.foryourlife.shared.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface JPAUserRepository extends JpaRepository<Participant,String>, JpaSpecificationExecutor<Participant> {
    Optional<Participant> findByUser_email (String email);

    Participant findByUser_Id(String userId);

    String user(User user);

    @Query("""
    SELECT COUNT(s) > 0 FROM Participant s
    WHERE s.id = :participantId
    AND NOT EXISTS (
        SELECT t FROM Team t
        JOIN t.training tr
        WHERE t MEMBER OF s.masterLifeTeams
        AND tr.startDate BETWEEN :startDate AND :endDate
        AND tr.id <> :newTrainingId
    )
""")
    boolean isMasterLifeAvailable(String participantId, LocalDate startDate, LocalDate endDate, String newTrainingId);
}
