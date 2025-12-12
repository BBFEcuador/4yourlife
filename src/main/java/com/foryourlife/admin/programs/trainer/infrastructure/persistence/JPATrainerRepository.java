package com.foryourlife.admin.programs.trainer.infrastructure.persistence;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.training.domain.EndDate;
import com.foryourlife.admin.programs.training.domain.StartDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JPATrainerRepository extends JpaRepository<Trainer, String>, JpaSpecificationExecutor<Trainer> {
    @Query("""
                SELECT t FROM Trainer t
                WHERE NOT EXISTS (
                    SELECT tm FROM Team tm
                    WHERE tm.trainer = t
                )
                OR t.id NOT IN (
                    SELECT DISTINCT tm.trainer.id FROM Team tm
                    JOIN tm.training tr
                    WHERE (
                        (:startDate BETWEEN tr.startDate AND tr.endDate)
                        OR (:endDate BETWEEN tr.startDate AND tr.endDate)
                        OR (tr.startDate BETWEEN :startDate AND :endDate)
                        OR (tr.endDate BETWEEN :startDate AND :endDate)
                    )
                )
            """)
    List<Trainer> findAvailableTrainers(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
//    @Query("""
//                SELECT COUNT(s) > 0 FROM Trainer s
//                WHERE s.id = :trainerId
//                AND NOT EXISTS (
//                    SELECT t FROM Team t
//                    JOIN t.training tr
//                    WHERE t MEMBER OF s.teams
//                    AND tr.startDate BETWEEN :startDate AND :endDate
//                    AND tr.id <> :newTrainingId
//                )
//            """)
//    boolean isTrainerAvailable(String trainerId, LocalDate startDate, LocalDate endDate, String newTrainingId);

    Optional<Trainer> findByEmail(String email);
}
