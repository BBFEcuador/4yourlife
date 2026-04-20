package com.foryourlife.masterLife.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.staff.domain.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MasterLifeRepository {
    void save(MasterLife staff);
    Optional<MasterLife> findById(String id);
    List<MasterLife> findAll();
    Page<MasterLife> findAll(Pageable pageable, Criteria criteria);
    Page<MasterLife> findAll(Pageable pageable);
    List<MasterLife> findAvailableMasterLife(LocalDate startDate, LocalDate endDate);

    List<MasterLife> match(Criteria criteria);

    void deleteById(String id);
    Optional<MasterLife> findByUserId(String participantId);
    boolean isMasterLifeAvailable(String staffId, LocalDate startDate, LocalDate endDate, String newTrainingId);
}
