package com.foryourlife.programs.infrastructure.httpControllers;

import com.foryourlife.programs.application.ProgramCreateService;
import com.foryourlife.programs.application.ProgramFinderService;
import com.foryourlife.programs.domain.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/program")
public class ProgramController {
    @Autowired
    private ProgramCreateService programService;

    @Autowired
    private ProgramFinderService programFinderService;


    @GetMapping("")
    public ResponseEntity<?> getPrograms() {
        return new ResponseEntity<>(programFinderService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        return new ResponseEntity<>(programFinderService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProgram(Program program) {
        programService.createProgram(program);
        return new ResponseEntity<>("message:'Program created successfully'", HttpStatus.CREATED);
    }
}
