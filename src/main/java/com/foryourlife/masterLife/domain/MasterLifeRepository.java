package com.foryourlife.masterLife.domain;

import com.foryourlife.shared.domain.criteria.Criteria;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MasterLifeRepository {
    void save(MasterLife staff);
    Optional<MasterLife> findById(String id);
    List<MasterLife> findAll();
    List<MasterLife> findAvailableMasterLife(LocalDate startDate, LocalDate endDate);

    List<MasterLife> match(Criteria criteria);

    void deleteById(String id);
    Optional<MasterLife> findByUserId(String participantId);
    boolean isMasterLifeAvailable(String staffId, LocalDate startDate, LocalDate endDate, String newTrainingId);
}
