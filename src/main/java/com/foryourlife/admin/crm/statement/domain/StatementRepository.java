package com.foryourlife.admin.crm.statement.domain;

import com.foryourlife.admin.programs.training.domain.Training;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StatementRepository {
    Optional<Statement> findById(String id);
    List<Statement> findByTrainingId(String trainingId);
    Page<Statement> findAll(Pageable pageable);
}
