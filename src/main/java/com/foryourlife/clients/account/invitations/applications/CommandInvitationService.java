package com.foryourlife.clients.account.invitations.applications;

import com.foryourlife.clients.account.invitations.domain.Invitation;
import com.foryourlife.clients.account.invitations.domain.InvitationRepository;
import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.clients.account.user.application.QueryUsersService;
import com.foryourlife.shared.domain.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommandInvitationService {
    private final InvitationRepository repository;
    private final QueryUsersService queryUsersService;
    private final Logger logger = LoggerFactory.getLogger(CommandUsersService.class);

    public CommandInvitationService(InvitationRepository repository, QueryUsersService queryUsersService) {
        this.repository = repository;
        this.queryUsersService = queryUsersService;
    }

    public String createInvitationByUser(String id){
        try {
            var user = queryUsersService.getUserById(id);
            var token = UUID.randomUUID().toString();
            var invitation = Invitation.create(UUID.randomUUID().toString(),token,null,false);
            repository.findByToken(token).orElseThrow(()
                    -> new BaseException("Problema al crear", List.of("El token "+invitation.getToken()+" ya existe"))
            );
            this.repository.save(invitation);
            return token;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String createInvitationByAdmin(String id){
        try {
            var token = UUID.randomUUID().toString();
            var invitation = Invitation.create(UUID.randomUUID().toString(),token,null,true);
            repository.findByToken(token).orElseThrow(()
                    -> new BaseException("Problema al crear", List.of("El token "+invitation.getToken()+" ya existe"))
            );
            this.repository.save(invitation);
            return token;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
