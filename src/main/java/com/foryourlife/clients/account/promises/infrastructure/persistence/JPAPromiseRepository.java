package com.foryourlife.clients.account.promises.infrastructure.persistence;

import com.foryourlife.clients.account.promises.domain.Promise;
import com.foryourlife.clients.account.promises.domain.PromiseRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JPAPromiseRepository implements PromiseRepository {
    private final JPAImplPromiseRepository jpaImplPromiseRepository;
    private final JPACriteriaConverter<Promise> criteriaConverter;


    public JPAPromiseRepository(JPAImplPromiseRepository jpaImplPromiseRepository, JPACriteriaConverter<Promise> criteriaConverter) {
        this.jpaImplPromiseRepository = jpaImplPromiseRepository;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public Promise save(Promise promise) {
        return this.jpaImplPromiseRepository.save(promise);
    }

    @Override
    public Optional<Promise> findById(String id) {
        return this.jpaImplPromiseRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        this.jpaImplPromiseRepository.deleteById(id);
    }

    @Override
    public Page<Promise> findAll(Pageable pageable, Criteria criteria) {
        return this.jpaImplPromiseRepository.findAll(criteriaConverter.getJpaSpecifications(criteria), pageable);
    }

    @Override
    public List<Promise> findByTrainingId(String trainingId) {
        return this.jpaImplPromiseRepository.findAllByTraining_Id(trainingId);
    }

    @Override
    public Optional<Promise> findLastByUserId(String userId) {
        LocalDate today = LocalDate.now();
        return this.jpaImplPromiseRepository.findFirstByUser_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateDesc(userId, today, today);
    }

    @Override
    public Optional<Promise> findLastByUserIdAndTrainingId(String userId, String trainingId) {
        return this.jpaImplPromiseRepository.findByUser_IdAndTraining_Id(userId, trainingId);
    }

    @Override
    public void saveAll(List<Promise> promises) {
        this.jpaImplPromiseRepository.saveAll(promises);
    }
}
