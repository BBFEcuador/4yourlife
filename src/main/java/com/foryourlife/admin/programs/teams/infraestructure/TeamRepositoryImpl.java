package com.foryourlife.admin.programs.teams.infraestructure;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.clients.account.user.infrastructure.JPAUserRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamRepositoryImpl implements TeamRepository {

    private final JPATeamRepository _jpaTeamRepository;
    private JPAUserRepository _jpaUserRepository;

    public TeamRepositoryImpl(JPATeamRepository _jpaTeamRepository, JPAUserRepository jpaUserRepository) {
        this._jpaTeamRepository = _jpaTeamRepository;
        _jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void save(Team team) {
        _jpaTeamRepository.save(team);
    }

    @Override
    public void update(Team team) {
        _jpaTeamRepository.save(team);
    }

    @Override
    public void assignParticipants(String teamId, String userId) {
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found",List.of("")));
        var users  = team.getUsers();
        users.add(_jpaUserRepository.findById(userId).orElseThrow(() -> new BaseException("User not found",List.of(""))));
        team.setUsers(users);
        _jpaTeamRepository.save(team);
    }

    @Override
    public void assignMastersLife(String teamId, String userId) {
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found",List.of("")));
        var masterlife  = team.getMasterLife();
        masterlife.add(_jpaUserRepository.findById(userId).orElseThrow(() -> new BaseException("MasterLife not found",List.of(""))));
        team.setMasterLife(masterlife);
        _jpaTeamRepository.save(team);
    }

    @Override
    public void removeParticipants(String teamId, String userId) {
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found",List.of("")));
        var users  = team.getUsers();
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
        var team = _jpaTeamRepository.findById(teamId).orElseThrow(() -> new BaseException("Team not found",List.of("")));
        var masterlife  = team.getMasterLife();
        for (var user : masterlife) {
            if (user.getId().equals(userId)) {
                masterlife.remove(user);
                break;
            }
        }
        team.setMasterLife(masterlife);
        _jpaTeamRepository.save(team);
    }

    @Override
    public Optional<Team> findById(String id) {
        return _jpaTeamRepository.findById(id);
    }

    @Override
    public List<Team> findAll() {
        return _jpaTeamRepository.findAll();
    }
}
