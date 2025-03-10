package com.foryourlife.clients.account.user.application;

import com.foryourlife.clients.account.invitations.applications.QueryInvitationServices;
import com.foryourlife.clients.account.module.application.ClientModuleCreatorService;
import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.participantLevel.application.ParticipantLevelService;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.user.domain.*;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.applications.CommandGeneralUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CommandUsersService {
    private final UserRepository _userRepository;
    private final CommandGeneralUserService commandGeneralUserService;
    private final QueryInvitationServices queryInvitationServices;
    private final ParticipantLevelService _rolRepository;
    private final ClientModuleCreatorService _clientModuleRepository;
    private final ParticipantLevelService participantLevelService;
    private final EventBus bus;
    private final Logger logger = LoggerFactory.getLogger(CommandUsersService.class);

    public CommandUsersService(UserRepository _userRepository, ParticipantLevelRepository rolRepository, CommandGeneralUserService commandUsersService, QueryInvitationServices queryInvitationServices, ParticipantLevelService rolRepository1, EventBus bus, ClientModuleCreatorService _clientModuleRepository, ParticipantLevelService participantLevelService) {
        this._userRepository = _userRepository;
        this.commandGeneralUserService = commandUsersService;
        this.queryInvitationServices = queryInvitationServices;
        _rolRepository = rolRepository1;
        this.bus = bus;
        this._clientModuleRepository = _clientModuleRepository;
        this.participantLevelService = participantLevelService;
    }
    @Transactional
    public void createInitUser(Participant user) {
        var token = queryInvitationServices.findInvitationByToken(user.getInvitationToken());
        if (token.getUsed())
            throw new BaseException("Token expired", List.of("The token " + user.getInvitationToken() + " was used"));
        commandGeneralUserService.save(user.getUser());

        var role = this._rolRepository.getInitRole();
        user.setParticipantLevel(role);
        this._userRepository.save(user);
        this._clientModuleRepository.createClientModule(ClientModule.create(UUID.randomUUID().toString(), false, false, false, user));
        this.bus.publish(user.pullDomainEvents());
    }

    public void save(Participant user) {
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

    public void update(Participant user) {
        if (this._userRepository.findByEmail(user.getId()).isEmpty())
            throw new UserNotFoundException("The Id: " + user.getId() + " doesn't exist.");
        try {
            var auxUser = this._userRepository.findById(user.getId()).get();
            this._userRepository.save(auxUser);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public LoginResponse login(String username, String password) throws BaseException {
        return this._userRepository.login(username, password);
    }

    public Participant getUser(String id) {
        return this._userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("The Id: " + id + " doesn't exist."));
    }

    public void setLevel(String userId, String levelId) {
        var user = this._userRepository.findById(userId).get();
        user.setParticipantLevel(participantLevelService.getRoleById(levelId));
        _userRepository.save(user);
    }
}
