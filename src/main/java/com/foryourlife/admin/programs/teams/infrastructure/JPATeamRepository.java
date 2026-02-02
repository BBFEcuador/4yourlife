package com.foryourlife.admin.programs.teams.infrastructure;

import com.foryourlife.admin.programs.teams.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPATeamRepository extends JpaRepository<Team, String>, JpaSpecificationExecutor<Team> {
    @Query("""
                SELECT t FROM Team t
                LEFT JOIN FETCH t.users
                WHERE t.training.id = :id
            """)
    Optional<Team> findByTraining_id(@Param("id") String id);

    List<Team> findAllByTrainer_Id(String trainerId);

    @Query("""
                SELECT t FROM Team t
                LEFT JOIN FETCH t.masterLife
                WHERE t.id = :id
            """)
    Optional<Team> findByIdWithMasterLife(@Param("id") String teamId);

    @Query("""
                SELECT t FROM Team t
                LEFT JOIN FETCH t.users
                WHERE t.id = :id
            """)
    Optional<Team> findByIdWithUsers(@Param("id") String id);
}
