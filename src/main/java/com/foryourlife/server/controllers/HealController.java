package com.foryourlife.server.controllers;

import com.foryourlife.clients.account.user.application.CommandUsersService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
public class HealController {

    private CommandUsersService createUser;
    private PasswordEncoder passwordEncoder;

    public HealController(CommandUsersService createUser, PasswordEncoder passwordEncoder) {
        this.createUser = createUser;
        this.passwordEncoder =passwordEncoder;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Ok";
    }
}
