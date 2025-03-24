package com.foryourlife.masterLife.infrastructure.httpController;

import com.foryourlife.admin.programs.trainer.infrastructure.httpControllers.AvailableTrainerRequest;
import com.foryourlife.masterLife.application.CommandMasterLifeService;
import com.foryourlife.masterLife.application.QueryMasterLifeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/master-life")
public class MasterLifeController {
    private final CommandMasterLifeService commandMasterLifeService;
    private final QueryMasterLifeService queryMasterLifeService;

    public MasterLifeController(CommandMasterLifeService commandMasterLifeService, QueryMasterLifeService queryMasterLifeService) {
        this.commandMasterLifeService = commandMasterLifeService;
        this.queryMasterLifeService = queryMasterLifeService;
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable String id) {
        commandMasterLifeService.changeStatus(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/available")
    public ResponseEntity<?> getForTeam(@Valid @RequestBody AvailableTrainerRequest request) {
        return new ResponseEntity<>(queryMasterLifeService.findavailableMasterLifes(request.startDate, request.endDate), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<?> createStaff(@RequestBody MasterLifeSaveRequest request) {
        commandMasterLifeService.save(request.toDomain());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("masterlife-participant/{id}")
    public ResponseEntity<?> createMasterLifeFromParticipant(@PathVariable String id) {
        commandMasterLifeService.saveFromParticipant(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("")
    public ResponseEntity<?> findStaffByUserId() {
        return new ResponseEntity<>(queryMasterLifeService.getAll(), HttpStatus.OK);
    }
}
