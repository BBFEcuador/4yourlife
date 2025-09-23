package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QueryTeamService {
    private final TeamRepository _teamRepository;
    private final Logger logger = LoggerFactory.getLogger(QueryTeamService.class);


    public QueryTeamService(TeamRepository _teamRepository) {
        this._teamRepository = _teamRepository;
    }
    @Transactional
    public Team getTeamById(String id) {
        var team =  this._teamRepository.findById(id).orElseThrow(
                () -> new BaseException("Not found", List.of("The team with id " + id + " does not exist")));
        Hibernate.initialize(team.getStaffs());
        Hibernate.initialize(team.getMasterLife());
        Hibernate.initialize(team.getVisionaries());
        return team;
    }

    public List<Team> getAllTeams() {
        return this._teamRepository.findAll();
    }
    public Page<Team> getAllTeams(Pageable pageable, Criteria criteria) {
        return this._teamRepository.findAll(pageable,criteria);
    }
    public List<Team> match(Criteria criteria) {
        return this._teamRepository.match(criteria);
    }

    public String getPhoto(String id) {
        return this._teamRepository.findById(id).orElseThrow(
                () -> new BaseException("Not found", List.of("The team with id " + id + " does not exist"))).getPhoto();
    }

    public Team getByTrainingId(String trainingId) {
        return this._teamRepository.findByTrainingId(trainingId).orElseThrow(
                () -> new BaseException("Not found", List.of("The team with training id " + trainingId + " does not exist")));
    }
}
