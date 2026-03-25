package com.foryourlife.admin.crm.statement.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StatementRepository {
    Optional<Statement> findById(String id);
    List<Statement> findByTrainingId(String trainingId);
    Page<Statement> findAll(Pageable pageable, Criteria criteria);
    void save(Statement statement);
    void saveAll(List<Statement> statements);
}
