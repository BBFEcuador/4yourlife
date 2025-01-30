package com.foryourlife.clients.account.contact.infrastructure.httpControllers;

import com.foryourlife.clients.account.contact.application.CommandContactService;
import com.foryourlife.clients.account.contact.application.QueryContactService;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final CommandContactService commandContactService;
    private final QueryContactService queryContactService;

    public ContactController(CommandContactService commandContactService, QueryContactService queryContactService) {
        this.commandContactService = commandContactService;
        this.queryContactService = queryContactService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(queryContactService.findAllByUser(userId), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveContact(@RequestBody SaveContactRequest request) {
        commandContactService.save(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/udpate")
    public ResponseEntity<?> updateContact(@RequestBody SaveContactRequest request) {
        if(!queryContactService.findById(request.getId()).isPresent()){
            throw new BaseException("Contact not found", List.of(request.getId()));
        }
            commandContactService.save(request.toDomain());
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable String id) {
        commandContactService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
