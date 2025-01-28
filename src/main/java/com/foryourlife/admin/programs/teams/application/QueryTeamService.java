package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.clients.account.user.application.QueryUsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryTeamService {
    private final TeamRepository _teamRepository;
    private final Logger logger = LoggerFactory.getLogger(QueryTeamService.class);


    public QueryTeamService(TeamRepository _teamRepository) {
        this._teamRepository = _teamRepository;
    }

    public Team getTeamById(String id) {
        return this._teamRepository.findById(id).orElseThrow();
    }

    public List<Team> getAllTeams() {
        return this._teamRepository.findAll();
    }
}
