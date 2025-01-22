package com.foryourlife.clients.account.user.infrastructure.httpControllers;

import com.foryourlife.clients.account.user.application.CommandUsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private CommandUsersService createUser;
    private PasswordEncoder passwordEncoder;

    public UserController(CommandUsersService createUser, PasswordEncoder passwordEncoder) {
        this.createUser = createUser;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody SaveUserRequest request) {
        request.password = passwordEncoder.encode((request.password));
        createUser.update(request.toDomain());
        return new ResponseEntity<>("message:'User updated successfully'", HttpStatus.CREATED);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        return new ResponseEntity<>(createUser.getUser(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody SaveUserRequest request) {
        request.password = passwordEncoder.encode((request.password));
        createUser.save(request.toDomain());
        return new ResponseEntity<>("message:'User created successfully'", HttpStatus.CREATED);
    }
}
