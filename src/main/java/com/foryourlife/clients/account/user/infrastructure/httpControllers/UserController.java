package com.foryourlife.clients.account.user.infrastructure.httpControllers;

import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.clients.account.user.application.QueryUsersService;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filters;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final QueryUsersService queryUsersService;

    public UserController(QueryUsersService queryUsersService) {
        this.queryUsersService = queryUsersService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(queryUsersService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        return new ResponseEntity<> (queryUsersService.getUserTrainerById(id), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<?> updateUser(@RequestBody SaveUserRequest participant) {
        queryUsersService.saveUser(participant.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/match")
    public ResponseEntity<?> match(@RequestBody Criteria criteria) {
        return new ResponseEntity<>(queryUsersService.matchers(criteria), HttpStatus.OK);
    }
}
