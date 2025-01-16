package com.foryourlife.account.user.infrastructure.httpControllers;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.account.user.application.CreateUser;
import com.foryourlife.account.user.domain.UserRepository;
import com.foryourlife.account.user.domain.Users;
import jakarta.validation.Valid;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private CreateUser createUser;
    private PasswordEncoder passwordEncoder;

    public UserController(CreateUser createUser, PasswordEncoder passwordEncoder) {
        this.createUser = createUser;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid SaveUserRequest request) {
        request.password = passwordEncoder.encode((request.password));
        createUser.update(request.toDomain());
        return new ResponseEntity<>("message:'User updated successfully'",HttpStatus.CREATED);
    }
}
