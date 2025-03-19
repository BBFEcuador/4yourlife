package com.foryourlife.clients.account.user.application;

import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.clients.account.invitations.applications.QueryInvitationServices;
import com.foryourlife.clients.account.module.application.ClientModuleCreatorService;
import com.foryourlife.clients.account.module.domain.ClientModule;
import com.foryourlife.clients.account.participantLevel.application.ParticipantLevelService;
import com.foryourlife.clients.account.participantLevel.domain.ParticipantLevelRepository;
import com.foryourlife.clients.account.user.domain.*;
import com.foryourlife.shared.domain.bus.EventBus;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.shared.domain.user.applications.CommandGeneralUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CommandUsersService {
    private final UserRepository _participantRepository;
    private final com.foryourlife.shared.domain.user.UserRepository _userRepository;
    private final CommandGeneralUserService commandGeneralUserService;
    private final QueryInvitationServices queryInvitationServices;
    private final ParticipantLevelService _rolRepository;
    private final ClientModuleCreatorService _clientModuleRepository;
    private final ParticipantLevelService participantLevelService;
    private final EventBus bus;
    private final Logger logger = LoggerFactory.getLogger(CommandUsersService.class);
    private final AdminRepository _adminRepository;

    public CommandUsersService(UserRepository _participantRepository, com.foryourlife.shared.domain.user.UserRepository _userRepository, CommandGeneralUserService commandGeneralUserService, QueryInvitationServices queryInvitationServices, ParticipantLevelService _rolRepository, ClientModuleCreatorService _clientModuleRepository, ParticipantLevelService participantLevelService, EventBus bus, AdminRepository _adminRepository) {
        this._participantRepository = _participantRepository;
        this._userRepository = _userRepository;
        this.commandGeneralUserService = commandGeneralUserService;
        this.queryInvitationServices = queryInvitationServices;
        this._rolRepository = _rolRepository;
        this._clientModuleRepository = _clientModuleRepository;
        this.participantLevelService = participantLevelService;
        this.bus = bus;
        this._adminRepository = _adminRepository;
    }

    @Transactional
    public void createInitUser(Participant user) {
        var token = queryInvitationServices.findInvitationByToken(user.getInvitationToken());
        if (token.getUsed())
            throw new BaseException("Token expired", List.of("The token " + user.getInvitationToken() + " was used"));
        commandGeneralUserService.save(user.getUser());

        var role = this._rolRepository.getInitRole();
        user.setParticipantLevel(role);
        this._participantRepository.save(user);
        this._clientModuleRepository.createClientModule(ClientModule.create(UUID.randomUUID().toString(), false, false, false, user));
        this.bus.publish(user.pullDomainEvents());
    }

    public void save(Participant user) {
        if (this._participantRepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyCreatedException("The email " + user.getEmail() + " is already registered");
        try {
            var ensureRolExist = this._rolRepository.getRoleById(user.getRoleId());
            this._participantRepository.save(user);

            this.bus.publish(user.pullDomainEvents());
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public void update(Participant user) {
        if (this._participantRepository.findByEmail(user.getId()).isEmpty())
            throw new UserNotFoundException("The Id: " + user.getId() + " doesn't exist.");
        try {
            var auxUser = this._participantRepository.findById(user.getId()).get();
            this._participantRepository.save(auxUser);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    public LoginResponse login(String username, String password) throws BaseException {
        return this._participantRepository.login(username, password);
    }

    public Participant getUser(String id) {
        return this._participantRepository.findById(id).orElseThrow(() -> new UserNotFoundException("The Id: " + id + " doesn't exist."));
    }

    public void setLevel(String userId, String levelId) {
        var user = this._participantRepository.findById(userId).get();
        user.setParticipantLevel(participantLevelService.getRoleById(levelId));
        _participantRepository.save(user);
    }

    public void createFromAdmin(Participant participant) {
        if (_participantRepository.findByUserId(participant.getUser().getId()) != null) {
            throw new BaseException("The user is already a Participant", List.of("Already exist as master life"));
        }

        var admin = _adminRepository.findByUserId(participant.getUser().getId()).orElseThrow(() -> new BaseException("User not found", List.of("User does not exist")));

        var user = admin.getUser();
        _userRepository.save(user);
        _participantRepository.save(participant);
    }

    public void promotionToMasterLife(String id){
        var user = this._participantRepository.findById(id).orElseThrow(() -> new BaseException("User not found",List.of()));
        var role = this._rolRepository.getRolByLevel(CourseLevel.MASTER_LIFE);
        user.setParticipantLevel(role);
        this._participantRepository.save(user);
    }
}
