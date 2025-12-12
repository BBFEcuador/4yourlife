package com.foryourlife.admin.programs.training.infrastructure;

import com.foryourlife.admin.programs.training.domain.StartDate;
import com.foryourlife.admin.programs.training.domain.Training;
import com.foryourlife.admin.programs.training.domain.TrainingRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingRepositoryImpl implements TrainingRepository {

    private final JPATrainingRepository repository;
    private final JPACriteriaConverter<Training> criteriaConverter;

    public TrainingRepositoryImpl(JPATrainingRepository repository, JPACriteriaConverter<Training> criteriaConverter) {
        this.repository = repository;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public void saveAll(List<Training> trainings) {
        this.repository.saveAll(trainings);
    }

    @Override
    public void save(Training trainings) {
        this.repository.save(trainings);
    }

    @Override
    public Page<Training> getAll(Pageable pageable, Criteria criteria) {
        return this.repository.findAll(criteriaConverter.getJpaSpecifications(criteria), pageable);
    }

    @Override
    public List<Training> match(Criteria criteria) {
        return repository.findAll(criteriaConverter.getJpaSpecifications(criteria));
    }

    @Override
    public Training matchOne(Criteria criteria) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        return repository.findAll(criteriaConverter.getJpaSpecifications(criteria), pageRequest).stream().findFirst().orElse(null);
    }

    @Override
    public List<Training> findByStartDate(LocalDate date) {
        return repository.findByStartDate(date);
    }

    @Override
    public List<Training> findByStartDateAndCampus_id(LocalDate date, String campusId) {
        return repository.findByStartDateAndCampus_id(date, campusId);
    }

    @Override
    public Optional<Training> findById(String id) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<Training> findByNextLevel_Id(String id) {
        return this.repository.findByNextLevel_Id(id);
    }
}
