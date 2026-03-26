package com.foryourlife.admin.crm.call.infrastructure.persistence;

import com.foryourlife.admin.crm.call.domain.Call;
import com.foryourlife.admin.crm.call.domain.CallRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JPACallRepository implements CallRepository {
    private JPAImplCallRepository jpaImplCallRepository;
    private JPACriteriaConverter<Call> jpaCriteriaConverter;

    public JPACallRepository(JPAImplCallRepository jpaImplCallRepository) {
        this.jpaImplCallRepository = jpaImplCallRepository;
    }

    @Override
    public void save(Call call) {
        jpaImplCallRepository.save(call);
    }

    @Override
    public Page<Call> findAll(Pageable pageable, Criteria criteria) {
        return jpaImplCallRepository.findAll(
                jpaCriteriaConverter.getJpaSpecifications(criteria),
                pageable
        );
    }

    @Override
    public Optional<Call> findById(String id) {
        return jpaImplCallRepository.findById(id);
    }

    @Override
    public List<Call> findAllByTrainingId(String trainingId) {
        return jpaImplCallRepository.findAllByTraining_Id(trainingId);
    }

    @Override
    public void saveAll(List<Call> calls) {
        jpaImplCallRepository.saveAll(calls);
    }
}
