package com.foryourlife.admin.programs.teams.infrastructure;

import com.foryourlife.admin.programs.teams.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPATeamRepository extends JpaRepository<Team, String>, JpaSpecificationExecutor<Team> {
    Optional<Team> findByTraining_id(String s);
    List<Team> findAllByTrainer_Id(String trainerId);
}
