package com.foryourlife.clients.account.invitations.applications;

import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.shared.domain.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryInvitationServices {
    private final InvitationRepository repository;
    private final Logger logger = LoggerFactory.getLogger(CommandUsersService.class);

    public QueryInvitationServices(InvitationRepository repository) {
        this.repository = repository;
    }

    public Invitation findInvitationByToken(String token) {
        return repository.findByToken(token).orElseThrow(() -> new BaseException("Invalid invitation token", List.of("The token " + token + " does not exist")));
    }

    public Invitation findActiveUserInvitation(String userId) {
        return repository.findTopBySenderIdOrderByQuantityDesc(userId).orElseThrow(() -> new BaseException("No tiene invitacion activa", List.of()));
    }
}
