package com.foryourlife.masterLife.application;

import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.masterLife.domain.MasterLifeRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QueryMasterLifeService {
    private final MasterLifeRepository repository;
    public QueryMasterLifeService(MasterLifeRepository repository) {
        this.repository = repository;
    }
    public MasterLife findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public MasterLife findByUserId(String userId) {
        return repository.findByUserId(userId).orElseThrow(() -> new BaseException("Master life not found", List.of()));
    }

    public List<MasterLife> getAll() {
        return repository.findAll();
    }
    public Page<MasterLife> getAll(Pageable pageable, Criteria criteria) {
        return repository.findAll(pageable,criteria);
    }

    public Page<MasterLife> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<MasterLife> match(Criteria criteria) {
        return repository.match(criteria);
    }

    public List<MasterLife> findavailableMasterLifes(LocalDate startDate, LocalDate endDate) {
        return repository.findAvailableMasterLife(startDate, endDate);
    }

    public boolean isMasterLifeAvailable(String staffId, LocalDate startDate, LocalDate endDate, String newTrainingId){
        return repository.isMasterLifeAvailable(staffId,startDate,endDate,newTrainingId);
    }
}
