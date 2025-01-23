package com.foryourlife.programs.infrastructure.persistence;

import com.foryourlife.programs.domain.Program;
import com.foryourlife.programs.domain.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramRepositoryImpl implements ProgramRepository {

    private final JPAProgramRepository impl;

    public ProgramRepositoryImpl(JPAProgramRepository impl) {
        this.impl = impl;
    }

    @Override
    public void save(Program program) {
        impl.save(program);
    }


    @Override
    public Optional<Program> findById(String id) {
        return impl.findById(id);
    }

    @Override
    public List<Program> getAll() {
        return impl.findAll();
    }

    @Override
    public void saveAll(List<Program> programs) {
        this.impl.saveAll(programs);
    }
}
