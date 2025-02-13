package com.foryourlife.admin.statement.application;

import com.foryourlife.admin.statement.domain.Statement;
import com.foryourlife.admin.statement.domain.StatementRepository;
import org.springframework.stereotype.Service;

@Service
public class StatementCreatorService {

    private final StatementRepository repository;

    public StatementCreatorService(StatementRepository repository) {
        this.repository = repository;
    }

    public void createStatement(Statement statement) {
        this.repository.saveStatement(statement);
    }
}
