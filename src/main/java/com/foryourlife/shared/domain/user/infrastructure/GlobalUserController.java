package com.foryourlife.shared.domain.user.infrastructure;

import com.foryourlife.shared.domain.user.applications.CommandGeneralUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/global-user")
public class GlobalUserController {

    private final CommandGeneralUserService command;

    public GlobalUserController(CommandGeneralUserService command) {
        this.command = command;
    }

    @PostMapping("mutate")
    public ResponseEntity<?> mutate(@Valid @RequestBody MutateUserRequest req){
        command.mutate(req);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
