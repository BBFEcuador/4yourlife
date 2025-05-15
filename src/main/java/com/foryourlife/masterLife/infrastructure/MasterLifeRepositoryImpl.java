package com.foryourlife.masterLife.infrastructure;

import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.masterLife.domain.MasterLifeRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MasterLifeRepositoryImpl implements MasterLifeRepository {

    private final JPAMasterLifeRepository repository;
    private final JPACriteriaConverter<MasterLife> criteriaConverter;

    public MasterLifeRepositoryImpl(JPAMasterLifeRepository repository, JPACriteriaConverter<MasterLife> criteriaConverter) {
        this.repository = repository;
        this.criteriaConverter = criteriaConverter;
    }

    @Override
    public void save(MasterLife staff) {
        repository.save(staff);
    }

    @Override
    public Optional<MasterLife> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<MasterLife> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<MasterLife> findAll(Pageable pageable, Criteria criteria) {
        return repository.findAll(criteriaConverter.getJpaSpecifications(criteria), pageable);
    }

    @Override
    public List<MasterLife> findAvailableMasterLife(LocalDate startDate, LocalDate endDate) {
        return repository.findAvailableMasterLife(startDate, endDate);
    }

    @Override
    public List<MasterLife> match(Criteria criteria) {
        return repository.findAll(criteriaConverter.getJpaSpecifications(criteria));
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public Optional<MasterLife> findByUserId(String participantId) {
        return repository.findByUser_Id(participantId);
    }

    @Override
    public boolean isMasterLifeAvailable(String staffId, LocalDate startDate, LocalDate endDate, String newTrainingId) {
        return repository.isMasterLifeAvailable(staffId, startDate, endDate, newTrainingId);
    }
}
