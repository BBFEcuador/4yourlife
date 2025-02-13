package com.foryourlife.admin.statement.application;

import com.foryourlife.admin.statement.domain.Statement;
import com.foryourlife.admin.statement.domain.StatementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementFinderService {
    private final StatementRepository repository;

    public StatementFinderService(StatementRepository repository) {
        this.repository = repository;
    }

    public List<Statement> findAll(){
        return repository.findAll();
    }

    public Statement findById(String id){
        return repository.findById(id).orElse(null);
    }
}
