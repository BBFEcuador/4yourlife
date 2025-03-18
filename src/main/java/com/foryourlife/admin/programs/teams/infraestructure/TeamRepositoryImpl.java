package com.foryourlife.admin.programs.teams.infraestructure;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.clients.account.user.infrastructure.JPAUserRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeamRepositoryImpl implements TeamRepository {

    private final JPATeamRepository _jpaTeamRepository;
    private final JPAUserRepository _jpaUserRepository;
    private final JPACriteriaConverter<Team> criteriaConverter;

    public TeamRepositoryImpl(JPATeamRepository _jpaTeamRepository, JPAUserRepository jpaUserRepository, JPACriteriaConverter<Team> criteriaConverter) {
        this._jpaTeamRepository = _jpaTeamRepository;
        _jpaUserRepository = jpaUserRepository;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public void save(Team team) {
        _jpaTeamRepository.save(team);
    }

    @Override
    public void updatePhoto(String id, String photo) {
        _jpaTeamRepository.save(_jpaTeamRepository.findById(id).map(team -> {
            team.setPhoto(photo);
            return team;
        }).orElseThrow(() -> new BaseException("Team not found", List.of(""))));
    }

    @Override
    public void assignParticipants(String teamId, String userId) {
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found", List.of("")));
        var users = team.getUsers();
        users.add(_jpaUserRepository.findById(userId).orElseThrow(() -> new BaseException("User not found", List.of(""))));
        team.setUsers(users);
        _jpaTeamRepository.save(team);
    }

    @Override
    public void assignMastersLife(String teamId, String userId) {
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found", List.of("")));
        var masterlife = team.getMasterLife();
        masterlife.add(_jpaUserRepository.findById(userId).orElseThrow(() -> new BaseException("MasterLife not found", List.of(""))));
        team.setMasterLife(masterlife);
        _jpaTeamRepository.save(team);
    }

    @Override
    public void removeParticipants(String teamId, String userId) {
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found", List.of("")));
        var users = team.getUsers();
        for (var user : users) {
            if (user.getId().equals(userId)) {
                users.remove(user);
                break;
            }
        }
        team.setUsers(users);
        _jpaTeamRepository.save(team);
    }

    @Override
    public void removeMastersLife(String teamId, String userId) {
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found", List.of("")));
        var masterlife = team.getMasterLife();
        for (var user : masterlife) {
            if (user.getId().equals(userId)) {
                masterlife.remove(user);
                break;
            }
        }
        team.setMasterLife(masterlife);
        _jpaTeamRepository.save(team);
    }
    @Transactional
    @Override
    public void removeStaffs(String teamId, String userId) {
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found", List.of("")));
        Hibernate.initialize(team.getStaffs());
        var staffs = team.getStaffs();
        for (var user : staffs) {
            if (user.getId().equals(userId)) {
                staffs.remove(user);
                break;
            }
        }
        team.setStaffs(staffs);
        _jpaTeamRepository.save(team);
    }
    @Transactional
    @Override
    public void removeVisionaries(String teamId, String id) {
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found", List.of("")));
        Hibernate.initialize(team.getVisionaries());
        var visionaries = team.getVisionaries();
        for (var user : visionaries) {
            if (user.getId().equals(id)) {
                visionaries.remove(user);
                break;
            }
        }
        team.setVisionaries(visionaries);
        _jpaTeamRepository.save(team);
    }

    @Override
    public Optional<Team> findById(String id) {
        return _jpaTeamRepository.findById(id);
    }

    @Override
    public Optional<Team> findByTrainingId(String id) {
        return Optional.empty();
    }

    @Override
    public List<Team> findAll() {
        return _jpaTeamRepository.findAll();
    }

    @Override
    public List<Team> match(Criteria criteria) {
        return _jpaTeamRepository.findAll(criteriaConverter.getJpaSpecifications(criteria));
    }
}
