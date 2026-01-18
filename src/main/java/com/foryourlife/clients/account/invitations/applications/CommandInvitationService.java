package com.foryourlife.clients.account.invitations.applications;

import com.foryourlife.admin.auth.application.AdminFinderService;
import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.invitations.domain.Sender;
import com.foryourlife.clients.account.invitations.infrastructure.GenericInvitationRequest;
import com.foryourlife.clients.account.invitations.infrastructure.InvitationRequest;
import com.foryourlife.clients.account.participant.application.ParticipantCommandService;
import com.foryourlife.clients.account.participant.application.ParticipantQueryService;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import com.foryourlife.shared.domain.user.UserRepository;
import jakarta.validation.Valid;
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

    private final TrainingRepository trainingRepository;
    private final Logger logger = LoggerFactory.getLogger(CommandInvitationService.class);

    public CommandInvitationService(InvitationRepository repository, ParticipantQueryService participantQueryService, UserRepository userRepository, QueryCampusService campusService, AdminFinderService adminFinderService, TrainingRepository trainingRepository) {
        this.repository = repository;
        this.participantQueryService = participantQueryService;
        this.userRepository = userRepository;
        this.campusService = campusService;
        this.adminFinderService = adminFinderService;
        this.trainingRepository = trainingRepository;
    }

    public String createInvitationByUser(String id, String campusId) {
        try {
            var user = participantQueryService.getParticipantById(id);
            var token = UUID.randomUUID().toString();
            var campus = campusService.findById(campusId);
            var invitation = Invitation.create(
                    UUID.randomUUID().toString(),
                    token,
                    null,
                    false,
                    id,
                    new Sender(
                            user.getId(),
                            user.getName(),
                            user.getTeam().getTraining().getName(),
                            user.getPhone()
                    ),
                    1,
                    campus);
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
        var invitation = Invitation.create(UUID.randomUUID().toString(), token, null, true, id, new Sender(user.getId(), user.getName(), "Administrativo", user.getEmail()), 1, campus);
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
                new Sender(user.getId(), user.getName(), "Administrativo", user.getEmail()), Integer.parseInt(request.quantity)
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
        var team = participant.getTeam();
        if (team == null) {
            throw new BaseException("El participante no tiene equipo asignado", List.of());
        }

        if (team.getTraining().getCourseLevel().equals(CourseLevel.FOCUS)
                || team.getTraining().getCourseLevel().equals(CourseLevel.YOUR)) {
            var userEntity = user.getEntityMap();
            var isMasterlife = userEntity != null && "MASTER_LIFE".equalsIgnoreCase(userEntity.toString());
            if (!isMasterlife) {
                throw new BaseException("El participante no puede crear invitaciones en este nivel", List.of());
            }
        }

        var invitation = Invitation.create(
                UUID.randomUUID().toString(),
                token,
                null,
                false,
                userId,
                new Sender(user.getId(), user.getName(), participant.getTeam().getTraining().getName(), user.getPhone()),
                Integer.parseInt(quantity), campus);
        this.repository.save(invitation);
        return token;
    }

    public String createGeneric(@Valid GenericInvitationRequest request) {
        var user = userRepository.findById(request.id).orElseThrow(() -> new BaseException("Usuario no encontrado", List.of()));
        var token = UUID.randomUUID().toString();
        var training = trainingRepository.findById(request.trainingId).orElseThrow(() -> new BaseException("Entrenamiento no encontrado", List.of()));
        var invitation = Invitation.create(
                UUID.randomUUID().toString(),
                token,
                null,
                true,
                request.id,
                new Sender(
                        user.getId(),
                        user.getName(),
                        training.getName(),
                        user.getEmail()
                ),
                Integer.parseInt(request.quantity),
                training.getCampus()
        );
        this.repository.save(invitation);
        return token;
    }
}
