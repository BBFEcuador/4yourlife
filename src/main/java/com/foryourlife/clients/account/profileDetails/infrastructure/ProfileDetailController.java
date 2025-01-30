package com.foryourlife.clients.account.profileDetails.infrastructure;

import com.foryourlife.clients.account.profileDetails.application.CommandProfileDetailsService;
import com.foryourlife.clients.account.profileDetails.application.QueryProfileDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-profile")
public class ProfileDetailController {

    private final CommandProfileDetailsService commandProfileDetailsService;
    private final QueryProfileDetailsService queryProfileDetailsService;

    public ProfileDetailController(CommandProfileDetailsService commandProfileDetailsService, QueryProfileDetailsService queryProfileDetailsService) {
        this.commandProfileDetailsService = commandProfileDetailsService;
        this.queryProfileDetailsService = queryProfileDetailsService;
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ProfileDetailRequest request, @PathVariable String id) {
        this.commandProfileDetailsService.updateProfile(request.toDomain(), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> find(@PathVariable String id) {
        var profile = this.queryProfileDetailsService.findById(id);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
