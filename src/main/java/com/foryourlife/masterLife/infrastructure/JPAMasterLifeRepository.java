package com.foryourlife.masterLife.infrastructure;

import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.staff.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JPAMasterLifeRepository extends JpaRepository<MasterLife,String>, JpaSpecificationExecutor<MasterLife> {
    Optional<MasterLife> findByUser_Id(String userId);
    @Query("""
                SELECT s FROM MasterLife s
                WHERE NOT EXISTS (
                    SELECT t FROM Team t
                    WHERE t MEMBER OF s.teams
                )
                OR s.id NOT IN (
                    SELECT DISTINCT s2.id FROM MasterLife s2
                    JOIN s2.teams t2
                    JOIN t2.training tr
                    WHERE (
                        (:startDate BETWEEN tr.startDate AND tr.endDate) 
                        OR (:endDate BETWEEN tr.startDate AND tr.endDate) 
                        OR (tr.startDate BETWEEN :startDate AND :endDate)
                        OR (tr.endDate BETWEEN :startDate AND :endDate)
                    )
                )
            """)
    List<MasterLife> findAvailableMasterLife(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("""
                SELECT COUNT(s) > 0 FROM MasterLife s
                WHERE s.id = :staffId
                AND NOT EXISTS (
                    SELECT t FROM Team t
                    JOIN t.training tr
                    WHERE t MEMBER OF s.teams
                    AND (
                        (:startDate BETWEEN tr.startDate AND tr.endDate)
                        OR (:endDate BETWEEN tr.startDate AND tr.endDate)
                        OR (tr.startDate BETWEEN :startDate AND :endDate)
                        OR (tr.endDate BETWEEN :startDate AND :endDate)
                    )
                    AND tr.id <> :newTrainingId
                )
            """)
    boolean isMasterLifeAvailable(String staffId, LocalDate startDate, LocalDate endDate, String newTrainingId);

    boolean existsByUser_IdAndIsActiveIsTrue(String userId);
}
