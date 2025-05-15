package com.foryourlife.admin.programs.trainer.infrastructure.persistence;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.admin.programs.trainer.domain.TrainerRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainerRepositoryImpl implements TrainerRepository {

    private final JPATrainerRepository repository;
    private final JPACriteriaConverter<Trainer> jpaCriteriaConverter;

    public TrainerRepositoryImpl(JPATrainerRepository repository, JPACriteriaConverter<Trainer> jpaCriteriaConverter) {
        this.repository = repository;
        this.jpaCriteriaConverter = jpaCriteriaConverter;
    }

    @Override
    public void saveTrainer(Trainer trainer) {
        this.repository.save(trainer);
    }

    @Override
    public Optional<Trainer> findTrainerById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public List<Trainer> getTrainers() {
        return this.repository.findAll();
    }

    @Override
    public Page<Trainer> getTrainers(Pageable pageable, Criteria criteria) {
        return this.repository.findAll(jpaCriteriaConverter.getJpaSpecifications(criteria),pageable);
    }

    @Override
    public List<Trainer> getAvailableTrainers(LocalDate startDate, LocalDate endDate) {
        return this.repository.findAvailableTrainers(startDate,endDate);
    }
}
