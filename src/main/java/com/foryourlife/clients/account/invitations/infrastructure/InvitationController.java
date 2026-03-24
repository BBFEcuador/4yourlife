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

    @PostMapping("/create-by-admin")
    public ResponseEntity<String> adminInvitation(
            @RequestParam String userId,
            @RequestParam String campusId) {
        return new ResponseEntity<>(this.service.createInvitationByAdmin(userId, campusId), HttpStatus.CREATED);
    }

    @PostMapping("/create-by-admin-quantity")
    public ResponseEntity<String> adminInvitation(@Valid @RequestBody InvitationRequest request) {
        return new ResponseEntity<>(this.service.createInvitationByAdminWithQuantity(request), HttpStatus.CREATED);
    }

    @PostMapping("/create-by-user-quantity")
    public ResponseEntity<String> userInvitation(@RequestParam String id, @RequestParam int quantity) {
        return new ResponseEntity<>(this.service.createInvitationByUserWithQuantity(id, quantity), HttpStatus.CREATED);
    }

    @PostMapping("/generic")
    public ResponseEntity<String> createGeneric(@Valid @RequestBody GenericInvitationRequest request) {
        return new ResponseEntity<>(this.service.createGeneric(request), HttpStatus.CREATED);
    }

    @GetMapping("/user/invitation/{id}")
    public ResponseEntity<?> getUserInvitation(@PathVariable String id) {
        return new ResponseEntity<>(this.queryInvitationServices.findActiveUserInvitation(id), HttpStatus.CREATED);
    }

    @GetMapping("{token}")
    public ResponseEntity<?> getInvitationByToken(@PathVariable String token) {
        return new ResponseEntity<>(this.queryInvitationServices.findInvitationByToken(token), HttpStatus.CREATED);
    }
}
