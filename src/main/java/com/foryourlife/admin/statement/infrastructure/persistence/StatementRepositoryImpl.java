package com.foryourlife.admin.statement.infrastructure.persistence;

import com.foryourlife.admin.statement.domain.Statement;
import com.foryourlife.admin.statement.domain.StatementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatementRepositoryImpl implements StatementRepository {

    private final JPAStatementRepository repository;

    public StatementRepositoryImpl(JPAStatementRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveStatement(Statement statement) {
        repository.save(statement);
    }

    @Override
    public List<Statement> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Statement> findById(String id) {
        return repository.findById(id);
    }
}
