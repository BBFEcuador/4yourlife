package com.foryourlife.clients.account.user.infrastructure.httpControllers;

import com.foryourlife.clients.account.user.application.CommandUsersService;
import com.foryourlife.clients.account.user.application.QueryUsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
}
