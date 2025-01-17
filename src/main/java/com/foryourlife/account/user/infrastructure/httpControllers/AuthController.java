package com.foryourlife.account.user.infrastructure.httpControllers;

import com.foryourlife.account.user.application.CreateUser;
import com.foryourlife.account.user.domain.LoginResponse;
import com.foryourlife.account.user.domain.UserRepository;
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
    private UserRepository _userRepository;

    public AuthController(PasswordEncoder passwordEncoder, CreateUser createUser, UserRepository _userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.createUser = createUser;
        this._userRepository=_userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        var response = this._userRepository.login(request.getUsername(), request.getPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody  SaveUserRequest request) {
        request.password = passwordEncoder.encode((request.password));
        createUser.save(request.toDomain());
        return new ResponseEntity<>("message:'User created successfully'",HttpStatus.CREATED);
    }
}
