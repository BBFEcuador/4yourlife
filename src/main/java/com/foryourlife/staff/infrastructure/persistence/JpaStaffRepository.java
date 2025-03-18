package com.foryourlife.staff.infrastructure.persistence;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.staff.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaStaffRepository extends JpaRepository<Staff, String>, JpaSpecificationExecutor<Staff> {
    Staff findByUser_Id(String userId);

    @Query("""
                SELECT s FROM Staff s
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
    List<Staff> findAvailableStaff(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("""
                SELECT COUNT(s) > 0 FROM Staff s
                WHERE s.id = :staffId
                AND NOT EXISTS (
                    SELECT t FROM Team t
                    JOIN t.training tr
                    WHERE t MEMBER OF s.teams
                    AND (
                        (:startDate BETWEEN tr.startDate.value AND tr.endDate.value)
                        OR (:endDate BETWEEN tr.startDate.value AND tr.endDate.value)
                        OR (tr.startDate.value BETWEEN :startDate AND :endDate)
                        OR (tr.endDate.value BETWEEN :startDate AND :endDate)
                    )
                    AND tr.id <> :newTrainingId
                )
            """)
    boolean isStaffAvailable(String staffId, LocalDate startDate, LocalDate endDate, String newTrainingId);
}
