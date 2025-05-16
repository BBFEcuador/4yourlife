package com.foryourlife.admin.sales.programs.application;

import com.foryourlife.admin.sales.programs.domain.Program;
import com.foryourlife.admin.sales.programs.domain.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramFinderService {
    private final ProgramRepository repository;

    public ProgramFinderService(ProgramRepository repository) {
        this.repository = repository;
    }

    public Optional<Program> findById(String id) {
        return repository.findById(id);
    }

    public List<Program> findAll() {
        return repository.getAll();
    }
}
