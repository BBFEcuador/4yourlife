package com.foryourlife.clients.account.invitations.infrastructure;

import com.foryourlife.clients.account.invitations.applications.CommandInvitationService;
import com.foryourlife.clients.account.invitations.applications.QueryInvitationServices;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    private final CommandInvitationService service;
    private final QueryInvitationServices queryInvitationServices;

    public InvitationController(CommandInvitationService service, QueryInvitationServices queryInvitationServices) {
        this.service = service;
        this.queryInvitationServices = queryInvitationServices;
    }

    @PostMapping("/create-by-admin/{id}")
    public ResponseEntity<String> adminInvitation(@PathVariable String id) {
        return new ResponseEntity<>(this.service.createInvitationByAdmin(id), HttpStatus.CREATED);
    }

    @PostMapping("/create-by-admin-quantity")
    public ResponseEntity<String> adminInvitation(@Valid @RequestBody InvitationRequest request) {
        return new ResponseEntity<>(this.service.createInvitationByAdminWithQuantity(request), HttpStatus.CREATED);
    }

    @PostMapping("/create-by-user-quantity")
    public ResponseEntity<String> userInvitation(@Valid @RequestBody InvitationRequest request) {
        return new ResponseEntity<>(this.service.createInvitationByUserWithQuantity(request), HttpStatus.CREATED);
    }

    @GetMapping("/user/invitation/{id}")
    public ResponseEntity<?> getUserInvitation(@PathVariable String id) {
        return new ResponseEntity<>(this.queryInvitationServices.findActiveUserInvitation(id), HttpStatus.CREATED);
    }
}
