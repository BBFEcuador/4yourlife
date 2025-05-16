package com.foryourlife.admin.sales.programs.domain;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository {
    void save(Program program);

    Optional<Program> findById(String id);

    List<Program> getAll();

    void saveAll(List<Program> programs);
}
