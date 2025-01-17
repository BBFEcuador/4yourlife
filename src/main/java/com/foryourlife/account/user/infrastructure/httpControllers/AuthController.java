package com.foryourlife.account.user.infrastructure.httpControllers;

import com.foryourlife.account.user.application.CreateUser;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private PasswordEncoder passwordEncoder;
    private CreateUser userService;

    public AuthController(PasswordEncoder passwordEncoder, CreateUser createUser) {
        this.passwordEncoder = passwordEncoder;
        this.userService = createUser;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request) throws BaseException {
        return new ResponseEntity<>(this.userService.login(request.getUsername(), request.getPassword()), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody  SaveUserRequest request) {
        request.password = passwordEncoder.encode((request.password));
        userService.save(request.toDomain());
        return new ResponseEntity<>("message:'User created successfully'", HttpStatus.CREATED);
    }
}
