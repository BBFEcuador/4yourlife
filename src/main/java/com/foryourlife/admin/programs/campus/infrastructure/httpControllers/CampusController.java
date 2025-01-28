package com.foryourlife.admin.programs.campus.infrastructure.httpControllers;

import com.foryourlife.admin.programs.campus.application.CommandCampusService;
import com.foryourlife.admin.programs.campus.application.QueryCampusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campus")
public class CampusController {

    private final QueryCampusService queryCampusService;
    private final CommandCampusService commandCampusService;

    public CampusController(QueryCampusService queryCampusService, CommandCampusService commandCampusService) {
        this.queryCampusService = queryCampusService;
        this.commandCampusService = commandCampusService;
    }

    @GetMapping("/getCampus")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(this.queryCampusService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/getCampus/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return new ResponseEntity<>(this.queryCampusService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CampusRequest request) {
        this.commandCampusService.save(request.toDomain());
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody CampusRequest request) {
        this.commandCampusService.update(request.toDomain());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
