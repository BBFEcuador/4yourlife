package com.foryourlife.admin.programs.trainer.infrastructure.persistence;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.training.domain.EndDate;
import com.foryourlife.admin.programs.training.domain.StartDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JPATrainerRepository extends JpaRepository<Trainer, String> {
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
                        (:startDate BETWEEN tr.startDate.value AND tr.endDate.value)
                        OR (:endDate BETWEEN tr.startDate.value AND tr.endDate.value) 
                        OR (tr.startDate.value BETWEEN :startDate AND :endDate)
                        OR (tr.endDate.value BETWEEN :startDate AND :endDate)
                    )
                )
            """)
    List<Trainer> findAvailableTrainers(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
