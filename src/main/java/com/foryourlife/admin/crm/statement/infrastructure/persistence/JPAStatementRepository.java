package com.foryourlife.admin.crm.statement.infrastructure.persistence;

import com.foryourlife.admin.crm.statement.domain.Statement;
import com.foryourlife.admin.crm.statement.domain.StatementRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPAStatementRepository implements StatementRepository {

    private final JPAImplStatementRepository repository;
    private final JPACriteriaConverter<Statement> criteriaConverter;

    public JPAStatementRepository(JPAImplStatementRepository repository, JPACriteriaConverter<Statement> criteriaConverter) {
        this.repository = repository;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public Optional<Statement> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Statement> findByTrainingId(String trainingId) {
        return repository.findByTraining_Id(trainingId);
    }

    @Override
    public Page<Statement> findAll(Pageable pageable, Criteria criteria) {
        return repository.findAll(criteriaConverter.getJpaSpecifications(criteria), pageable);
    }

    @Override
    public void save(Statement statement) {
        repository.save(statement);
    }

    @Override
    public void saveAll(List<Statement> statements) {
        repository.saveAll(statements);
    }
}