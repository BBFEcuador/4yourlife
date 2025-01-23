package com.foryourlife.clients.account.invitations.applications;

import com.foryourlife.admin.auth.application.AdminFinderService;
import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.invitations.domain.Sender;
import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.clients.account.user.application.QueryUsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommandInvitationService {
    private final InvitationRepository repository;
    private final QueryUsersService queryUsersService;
    private final AdminFinderService adminFinderService;
    private final Logger logger = LoggerFactory.getLogger(CommandUsersService.class);

    public CommandInvitationService(InvitationRepository repository, QueryUsersService queryUsersService, AdminFinderService adminFinderService) {
        this.repository = repository;
        this.queryUsersService = queryUsersService;
        this.adminFinderService = adminFinderService;
    }

    public String createInvitationByUser(String id) {
        try {
            var user = queryUsersService.getUserById(id);
            var token = UUID.randomUUID().toString();
            var invitation = Invitation.create(UUID.randomUUID().toString(), token, null, false, id, new Sender(user.getName(), user.getPhone()));
            this.repository.save(invitation);
            return token;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String createInvitationByAdmin(String id) {
        try {
            var user = adminFinderService.findById(id);
            var token = UUID.randomUUID().toString();
            var invitation = Invitation.create(UUID.randomUUID().toString(), token, null, true, id, new Sender(user.getName(), user.getEmail()));
            this.repository.save(invitation);
            return token;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
