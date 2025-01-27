package com.foryourlife.admin.programs.teams.infraestructure;

import com.foryourlife.admin.programs.teams.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPATeamRepository extends JpaRepository<Team, String> {

}
