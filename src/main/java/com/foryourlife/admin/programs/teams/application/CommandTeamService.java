package com.foryourlife.admin.programs.teams.application;

import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.teams.domain.TeamRepository;
import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.clients.account.user.domain.UserRepository;
import com.foryourlife.clients.account.user.domain.Users;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandTeamService {

    private final TeamRepository _teamRepository;
    private final EventBus bus;
    private final UserRepository _userRepository;
    private final Logger logger = LoggerFactory.getLogger(CommandTeamService.class);

    public CommandTeamService(TeamRepository _teamRepository, EventBus bus, UserRepository _userRepository) {
        this._teamRepository = _teamRepository;
        this.bus = bus;
        this._userRepository = _userRepository;
    }

    public void save(Team team) {
        this._teamRepository.save(team);
        this.bus.publish(team.pullDomainEvents());
    }

    public void update(Team team) {
        if (!this._teamRepository.findById(team.getId()).isPresent()) {
            throw new BaseException("Team not found", List.of(""));
        }
        this._teamRepository.save(team);
    }

    public void assignParticipants(String teamId, String userId) {
        var team = _teamRepository.findById(teamId);
        if (!team.isPresent()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (!_userRepository.findById(userId).isPresent()) {
            throw new BaseException("User not found", List.of(""));
        }
        team.orElseThrow().getUsers().forEach(user -> {
            if (user.getId().equals(userId)) {
                throw new BaseException("User already assigned", List.of(""));
            }
        });
        _teamRepository.assignParticipants(teamId, userId);
    }

    public void assignMastersLife(String teamId, String userId) {
        if (!this._teamRepository.findById(teamId).isPresent()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (!_userRepository.findById(userId).isPresent()) {
            throw new BaseException("User not found", List.of(""));
        }
        Users user = _userRepository.findById(userId).get();
        if (user.getParticipantLevel().getCourseLevel() == CourseLevel.MASTER_LIFE) {
            _teamRepository.assignMastersLife(teamId, userId);
        } else {
            throw new BaseException("User is not MasterLife", List.of(""));
        }
    }

    public void removeParticipants(String teamId, String userId) {
        if (!this._teamRepository.findById(teamId).isPresent()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (!_userRepository.findById(userId).isPresent()) {
            throw new BaseException("User not found", List.of(""));
        }
        _teamRepository.removeParticipants(teamId, userId);
    }

    public void removeMastersLife(String teamId, String userId) {
        if (!this._teamRepository.findById(teamId).isPresent()) {
            throw new BaseException("Team not found", List.of(""));
        } else if (!_userRepository.findById(userId).isPresent()) {
            throw new BaseException("User not found", List.of(""));
        }
        _teamRepository.removeMastersLife(teamId, userId);
    }
}
