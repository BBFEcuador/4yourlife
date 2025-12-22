package com.foryourlife.clients.account.participant.infrastructure.httpControllers;

import com.foryourlife.clients.account.participant.application.ParticipantCommandService;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private PasswordEncoder passwordEncoder;
    private ParticipantCommandService userService;

    public AuthController(PasswordEncoder passwordEncoder, ParticipantCommandService createUser) {
        this.passwordEncoder = passwordEncoder;
        this.userService = createUser;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody ParticipantLoginRequest request) throws BaseException {
        return new ResponseEntity<>(this.userService.login(request.getUsername(), request.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody SaveParticipantRequest request) {
        request.password = passwordEncoder.encode(request.password);

        userService.createInitUser(
                request.toDomain(),
                request.getMedicalRecord(),
                request.contact,
                request.getDataInvoice() != null ? request.getDataInvoice().toDomain() : null
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
