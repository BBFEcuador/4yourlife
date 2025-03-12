package com.foryourlife.visionary.infrastructure.persistence;

import com.foryourlife.staff.domain.Staff;
import com.foryourlife.visionary.domain.Visionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaVisionaryRepository extends JpaRepository<Visionary, String> {
    List<Visionary> findAllByUser_Id(String userId);
    @Query("""
                SELECT s FROM Visionary s
                WHERE NOT EXISTS (
                    SELECT t FROM Team t
                    WHERE t MEMBER OF s.teams
                )
                OR s.id NOT IN (
                    SELECT DISTINCT s2.id FROM Staff s2
                    JOIN s2.teams t2
                    JOIN t2.training tr
                    WHERE (
                        (:startDate BETWEEN tr.startDate.value AND tr.endDate.value) 
                        OR (:endDate BETWEEN tr.startDate.value AND tr.endDate.value) 
                        OR (tr.startDate.value BETWEEN :startDate AND :endDate)
                        OR (tr.endDate.value BETWEEN :startDate AND :endDate)
                    )
                )
            """)
    List<Visionary> findAvailableVisionaries(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
