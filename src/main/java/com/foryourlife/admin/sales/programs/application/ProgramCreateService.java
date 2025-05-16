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
        try {
            repository.save(program);
            logger.info("Program created successfully: {}", program.getName());

        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void updateProgram(Program program) {
        try {
            Optional<Program> existingProgram = repository.findById(program.getId());
            if (existingProgram.isEmpty()) {
                logger.error("Program not found: {}", program.getId());
                return;
            }
            existingProgram.get().setName(program.getName());
            existingProgram.get().setLevel(program.getLevel());
            repository.save(existingProgram.orElse(null));
            logger.info("Program updated successfully: {}", program.getName());

        }catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
