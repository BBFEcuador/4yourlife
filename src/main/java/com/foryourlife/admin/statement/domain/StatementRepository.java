package com.foryourlife.admin.statement.domain;

import java.util.List;
import java.util.Optional;

public interface StatementRepository {

    void saveStatement(Statement statement);

    List<Statement> findAll();

    Optional<Statement> findById(String id);
}
