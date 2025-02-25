package com.foryourlife.clients.account.user.infrastructure.httpControllers;

import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.clients.account.user.application.QueryUsersService;
import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final QueryUsersService queryUsersService;
    private final CommandUsersService commandUsersService;

    public UserController(QueryUsersService queryUsersService, CommandUsersService commandUsersService) {
        this.queryUsersService = queryUsersService;
        this.commandUsersService = commandUsersService;
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
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest participant) {
        commandUsersService.update(participant.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/level/{id}/{levelId}")
    public ResponseEntity<?> setLevel(@PathVariable String id, @PathVariable String levelId){
        System.out.println(levelId);
        commandUsersService.setLevel(id, levelId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/match")
    public ResponseEntity<?> match(@RequestBody Criteria criteria) {
        return new ResponseEntity<>(queryUsersService.matchers(criteria), HttpStatus.OK);
    }
}
