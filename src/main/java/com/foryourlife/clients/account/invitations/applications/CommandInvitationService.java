package com.foryourlife.clients.account.invitations.applications;

import com.foryourlife.admin.auth.application.AdminFinderService;
import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.invitations.domain.Sender;
import com.foryourlife.clients.account.invitations.infrastructure.InvitationRequest;
import com.foryourlife.clients.account.participant.application.ParticipantCommandService;
import com.foryourlife.clients.account.participant.application.ParticipantQueryService;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommandInvitationService {
    private final InvitationRepository repository;
    private final ParticipantQueryService participantQueryService;
    private final UserRepository userRepository;
    private final QueryCampusService campusService;
    private final AdminFinderService adminFinderService;
    private final Logger logger = LoggerFactory.getLogger(ParticipantCommandService.class);

    public CommandInvitationService(InvitationRepository repository, ParticipantQueryService participantQueryService, UserRepository userRepository, QueryCampusService campusService, AdminFinderService adminFinderService) {
        this.repository = repository;
        this.participantQueryService = participantQueryService;
        this.userRepository = userRepository;
        this.campusService = campusService;
        this.adminFinderService = adminFinderService;
    }

    public String createInvitationByUser(String id, String campusId) {
        try {
            var user = participantQueryService.getParticipantById(id);
            var token = UUID.randomUUID().toString();
            var campus = campusService.findById(campusId);
            var invitation = Invitation.create(UUID.randomUUID().toString(), token, null, false, id, new Sender(user.getId(), user.getName(), user.getPhone()), 1, campus);
            this.repository.save(invitation);
            return token;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String createInvitationByAdmin(String id, String campusId) {
        var user = adminFinderService.findById(id);
        var token = UUID.randomUUID().toString();
        var campus = campusService.findById(campusId);
        var invitation = Invitation.create(UUID.randomUUID().toString(), token, null, true, id, new Sender(user.getId(), user.getName(), user.getEmail()), 1, campus);
        this.repository.save(invitation);
        return token;
    }

    public String createInvitationByAdminWithQuantity(InvitationRequest request) {
        var user = adminFinderService.findById(request.id);
        var token = UUID.randomUUID().toString();
        var campus = campusService.findById(request.campusId);
        var invitation = Invitation.create(
                UUID.randomUUID().toString(),
                token,
                null,
                true,
                request.id,
                new Sender(user.getId(), user.getName(), user.getEmail()), Integer.parseInt(request.quantity)
                , campus);
        this.repository.save(invitation);
        return token;
    }

    public String createInvitationByUserWithQuantity(String userId, String quantity) {
        var invitations = this.repository.findBySenderId(userId)
                .stream()
                .anyMatch(invitation -> invitation.getQuantity() > 0);
        if (invitations) {
            throw new BaseException("Tiene invitacion activa", List.of());
        }
        var user = userRepository.findById(userId).orElseThrow(
                () -> new BaseException("Usuario no encontrado", List.of())
        );
        var participant = participantQueryService.getByUserId(userId);
        var token = UUID.randomUUID().toString();
        var campus = campusService.findById(participant.getCampus().getId());
        var invitation = Invitation.create(
                UUID.randomUUID().toString(),
                token,
                null,
                false,
                userId,
                new Sender(user.getId(), user.getName(), user.getPhone()),
                Integer.parseInt(quantity),campus);
        this.repository.save(invitation);
        return token;
    }
}
