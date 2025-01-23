package com.foryourlife.programs.domain;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository {
    void save(Program program);

    Optional<Program> findById(String id);

    List<Program> getAll();

    void saveAll(List<Program> programs);
}
