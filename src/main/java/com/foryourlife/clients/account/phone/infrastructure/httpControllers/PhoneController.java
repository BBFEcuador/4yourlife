package com.foryourlife.clients.account.phone.infrastructure.httpControllers;

import com.foryourlife.clients.account.phone.application.CommandPhoneService;
import com.foryourlife.clients.account.phone.application.QueryPhoneService;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone")
public class PhoneController {
    private final CommandPhoneService commandPhoneService;
    private final QueryPhoneService queryPhoneService;

    public PhoneController(CommandPhoneService commandPhoneService, QueryPhoneService queryPhoneService) {
        this.commandPhoneService = commandPhoneService;
        this.queryPhoneService = queryPhoneService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllByUser(@PathVariable String userId) {
        return new ResponseEntity<>(queryPhoneService.findAllByUser(userId), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> savePhone(@RequestBody SavePhoneRequest request) {
        commandPhoneService.save(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePhone(@RequestBody SavePhoneRequest request) {
        if(!queryPhoneService.findById(request.getId()).isPresent()){
            throw new BaseException("Phone not found", List.of(request.getId()));
        }
        commandPhoneService.save(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePhone(@PathVariable String id) {
        commandPhoneService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
