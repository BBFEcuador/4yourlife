package com.foryourlife.server.controllers;

import com.foryourlife.clients.account.participant.application.ParticipantCommandService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
public class HealController {

    private ParticipantCommandService createUser;
    private PasswordEncoder passwordEncoder;

    public HealController(ParticipantCommandService createUser, PasswordEncoder passwordEncoder) {
        this.createUser = createUser;
        this.passwordEncoder =passwordEncoder;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Ok";
    }
}
