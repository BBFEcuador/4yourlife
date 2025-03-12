package com.foryourlife.admin.programs.teams.infraestructure.httpControllers;

import com.foryourlife.admin.programs.teams.application.CommandTeamService;
import com.foryourlife.admin.programs.teams.application.QueryTeamService;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final CommandTeamService commandTeamService;
    private final QueryTeamService queryTeamService;

    public TeamController(CommandTeamService commandTeamService, QueryTeamService queryTeamService) {
        this.commandTeamService = commandTeamService;
        this.queryTeamService = queryTeamService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(this.queryTeamService.getAllTeams(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return new ResponseEntity<>(this.queryTeamService.getTeamById(id), HttpStatus.OK);
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhoto(@PathVariable String id) {
        return new ResponseEntity<>(this.queryTeamService.getPhoto(id), HttpStatus.OK);
    }

    @PostMapping("/save/focus")
    public ResponseEntity<?> saveTeamFocus(@Valid @RequestBody SaveFocusTeamsRequest request) {
        commandTeamService.saveFocusTeam(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/save/your")
    public ResponseEntity<?> saveTeamYour(@Valid @RequestBody SaveYourTeamRequest request) {
        commandTeamService.saveYourTeam(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/match")
    public ResponseEntity<?> match(@Valid @RequestBody Criteria request) {
        return new ResponseEntity<>(queryTeamService.match(request),HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTeam(@RequestBody SaveTeamRequest request) {
        commandTeamService.update(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/updatePhoto/{id}")
    public ResponseEntity<?> updatePhoto(@PathVariable String id, @RequestParam("photo") MultipartFile photo) {
        try {
            return new ResponseEntity<>(commandTeamService.updatePhoto(id, photo), HttpStatus.CREATED);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), List.of(""));
        }
    }

    @PostMapping("/assignParticipants")
    public ResponseEntity<?> assignParticipants(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.assignParticipants(request.getTeamId(), user.getId());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/assignMaterlife")
    public ResponseEntity<?> assignMasterlife(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.assignMastersLife(request.getTeamId(), user.getId());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/removeParticipants")
    public ResponseEntity<?> removeParticipants(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.removeParticipants(request.getTeamId(), user.getId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/removeMasterlife")
    public ResponseEntity<?> removeMasterlife(@RequestBody AssignParticipantsRequest request) {
        for (var user : request.getUsers()) {
            commandTeamService.removeMastersLife(request.getTeamId(), user.getId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
