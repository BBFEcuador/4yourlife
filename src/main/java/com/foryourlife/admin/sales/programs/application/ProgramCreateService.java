package com.foryourlife.admin.sales.programs.application;

import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.admin.sales.programs.domain.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProgramCreateService {
    private final ProgramRepository repository;
    private final Logger logger = LoggerFactory.getLogger(ProgramCreateService.class);

    public ProgramCreateService(ProgramRepository repository) {
        this.repository = repository;
    }

    public void createProgram(Program program) {

    }

    public void updateProgram(Program program) {

    }
}
