package com.foryourlife.server.controllers;

import com.foryourlife.account.user.application.CreateUser;
import com.foryourlife.account.user.infrastructure.httpControllers.SaveUserRequest;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
public class HealController {

    private CreateUser createUser;
    private PasswordEncoder passwordEncoder;

    public HealController(CreateUser createUser, PasswordEncoder passwordEncoder) {
        this.createUser = createUser;
        this.passwordEncoder =passwordEncoder;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Ok";
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody @Valid SaveUserRequest request) {
        request.password = passwordEncoder.encode((request.password));
        createUser.save(request.toDomain());
        return new ResponseEntity<>("message:'User created successfully'",HttpStatus.CREATED);
    }
}
