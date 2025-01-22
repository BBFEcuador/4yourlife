package com.foryourlife.clients.account.invitations.infrastructure;

import com.foryourlife.clients.account.invitations.applications.CommandInvitationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    private final CommandInvitationService service;

    public InvitationController(CommandInvitationService service) {
        this.service = service;
    }

    @PostMapping("/create-by-user/{id}")
    public ResponseEntity<String> userInvitation(@PathVariable String id){
        return new ResponseEntity<>(this.service.createInvitationByUser(id),HttpStatus.CREATED);
    }

    @PostMapping("/create-by-admin/{id}")
    public ResponseEntity<String> adminInvitation(@PathVariable String id){
        return new ResponseEntity<>(this.service.createInvitationByAdmin(id),HttpStatus.CREATED);
    }
}
