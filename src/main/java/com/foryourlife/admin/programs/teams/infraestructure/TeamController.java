package com.foryourlife.admin.programs.teams.infraestructure;

import com.foryourlife.admin.programs.teams.application.CommandTeamService;
import com.foryourlife.admin.programs.teams.domain.Team;
import com.foryourlife.admin.programs.training.application.QueryTrainingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/admin/team")
public class TeamController {

    private final CommandTeamService commandTeamService;
    private final QueryTrainingService queryTrainingService;

    public TeamController(CommandTeamService commandTeamService, QueryTrainingService queryTrainingService) {
        this.commandTeamService = commandTeamService;
        this.queryTrainingService = queryTrainingService;
    }

    @PostMapping("")
    public ResponseEntity<?> add(@Valid @RequestBody SaveTeamRequest request) {
        var training = queryTrainingService.getTrainingById(request.trainingId);
        var team = Team.create(request.id != null ? request.id : UUID.randomUUID().toString(), request.name, request.photo, training, training.getNumber(), Set.of(), Set.of());
        commandTeamService.save(team);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
