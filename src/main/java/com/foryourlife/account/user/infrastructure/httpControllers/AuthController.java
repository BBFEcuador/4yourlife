package com.foryourlife.account.user.infrastructure.httpControllers;

import com.foryourlife.account.user.application.CreateUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private PasswordEncoder passwordEncoder;
    private CreateUser createUser;

    public AuthController(PasswordEncoder passwordEncoder, CreateUser createUser) {
        this.passwordEncoder = passwordEncoder;
        this.createUser = createUser;
    }

    @GetMapping("/login")
    public String login(){
        return "ok";
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody @Valid SaveUserRequest request) {
        request.password = passwordEncoder.encode((request.password));
        createUser.save(request.toDomain());
        return new ResponseEntity<>("message:'User created successfully'",HttpStatus.CREATED);
    }
}
