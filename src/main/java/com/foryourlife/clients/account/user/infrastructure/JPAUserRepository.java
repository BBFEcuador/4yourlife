package com.foryourlife.clients.account.user.infrastructure;

import com.foryourlife.clients.account.user.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JPAUserRepository extends JpaRepository<Participant, String>, JpaSpecificationExecutor<Participant> {
    Optional<Participant> findByUser_email(String email);

    Participant findByUser_Id(String userId);

    @Query("""
                SELECT s FROM Participant s
                JOIN s.participantLevel pl
                WHERE pl.courseLevel = 'MASTER_LIFE'
                AND(NOT EXISTS (
                    SELECT t FROM Team t
                    WHERE t MEMBER OF s.masterLife
                )
                OR s.id NOT IN (
                    SELECT DISTINCT s2.id FROM Participant s2
                    JOIN s2.masterLife t2
                    JOIN t2.training tr
                    WHERE (
                        (:startDate BETWEEN tr.startDate.value AND tr.endDate.value)
                        OR (:endDate BETWEEN tr.startDate.value AND tr.endDate.value) 
                        OR (tr.startDate.value BETWEEN :startDate AND :endDate)
                        OR (tr.endDate.value BETWEEN :startDate AND :endDate)
                    )
                ))
            """)
    List<Participant> findAvailableMasterLife(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("""
                SELECT COUNT(s) > 0 FROM Participant s
                WHERE s.id = :participantId
                AND NOT EXISTS (
                    SELECT t FROM Team t
                    JOIN t.training tr
                    WHERE t MEMBER OF s.masterLife
                    AND (
                           (:startDate BETWEEN tr.startDate.value AND tr.endDate.value)
                            OR (:endDate BETWEEN tr.startDate.value AND tr.endDate.value)
                            OR (tr.startDate.value BETWEEN :startDate AND :endDate)
                            OR (tr.endDate.value BETWEEN :startDate AND :endDate)
                           )
                    AND tr.id <> :newTrainingId
                )
            """)
    boolean isMasterLifeAvailable(String participantId, LocalDate startDate, LocalDate endDate, String newTrainingId);
}
