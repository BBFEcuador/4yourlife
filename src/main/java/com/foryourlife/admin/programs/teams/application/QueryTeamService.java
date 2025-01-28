package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.shared.domain.exception.BaseException;
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
        return this._teamRepository.findById(id).orElseThrow(
                () -> new BaseException("Not found", List.of("The team with id " + id + " does not exist")));
    }

    public List<Team> getAllTeams() {
        return this._teamRepository.findAll();
    }
}
