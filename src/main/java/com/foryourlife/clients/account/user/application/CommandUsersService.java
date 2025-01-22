package com.foryourlife.clients.account.user.application;

import com.foryourlife.clients.account.participantLevel.application.ParticipantLevelService;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.user.domain.*;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommandUsersService {
    private final UserRepository _userRepository;
    private final ParticipantLevelService _rolRepository;
    private final EventBus bus;
    private final Logger logger = LoggerFactory.getLogger(CommandUsersService.class);

    public CommandUsersService(UserRepository _userRepository, ParticipantLevelRepository rolRepository, ParticipantLevelService rolRepository1, EventBus bus) {
        this._userRepository = _userRepository;
        _rolRepository = rolRepository1;
        this.bus = bus;
    }

    public void createInitUser(Users user){
        if (this._userRepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyCreatedException("The email " + user.getEmail() + "is already registered");
        try {
            var role = this._rolRepository.getInitRole();
            user.setParticipantLevel(role);
            this._userRepository.save(user);
            this.bus.publish(user.pullDomainEvents());
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public void save(Users user) {
        if (this._userRepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyCreatedException("The email " + user.getEmail() + " is already registered");
        try {
            var ensureRolExist = this._rolRepository.getRoleById(user.getRoleId());
            this._userRepository.save(user);
            this.bus.publish(user.pullDomainEvents());
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public void update(Users user) {
        if (this._userRepository.findById(user.getId()).isEmpty())
            throw new UserNotFoundException("The Id: " + user.getId() + " doesn't exist.");
        try {
            this._userRepository.save(user);
            this.bus.publish(user.pullDomainEvents());
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public LoginResponse login(String username, String password) throws BaseException {
            return this._userRepository.login(username, password);
    }

    public Users getUser(String id) {
        return this._userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("The Id: " + id + " doesn't exist."));
    }
}
