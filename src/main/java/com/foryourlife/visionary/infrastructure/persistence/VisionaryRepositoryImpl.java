package com.foryourlife.visionary.infrastructure.persistence;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VisionaryRepositoryImpl implements VisionaryRepository {
    private final JpaVisionaryRepository repository;
    private final JPACriteriaConverter<Visionary> converter;

    public VisionaryRepositoryImpl(JpaVisionaryRepository repository, JPACriteriaConverter<Visionary> converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public void save(Visionary visionary) {
        repository.save(visionary);
    }

    @Override
    public Optional<Visionary> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Visionary> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Visionary> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Visionary> findAll(Pageable pageable, Criteria criteria) {
        return repository.findAll(converter.getJpaSpecifications(criteria),pageable);
    }

    @Override
    public List<Visionary> findAvailableVisionaries(LocalDate startDate, LocalDate endDate) {
        return repository.findAvailableVisionaries(startDate,endDate);
    }

    @Override
    public List<Visionary> match(Criteria criteria) {
        return repository.findAll(converter.getJpaSpecifications(criteria));
    }

    @Override
    public Optional<Visionary> findByUserId(String userId) {
        return repository.findByUser_Id(userId);
    }

    @Override
    public boolean isVisionaryAvailable(String visionaryId, LocalDate startDate, LocalDate endDate, String newTrainingId) {
        return repository.isVisionaryAvailable(visionaryId, startDate, endDate, newTrainingId);
    }
}
