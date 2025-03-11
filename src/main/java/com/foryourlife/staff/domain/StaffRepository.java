package com.foryourlife.staff.domain;

import com.foryourlife.shared.domain.criteria.Criteria;

import java.util.List;
import java.util.Optional;

public interface StaffRepository {
    void save(Staff staff);
    Optional<Staff> findById(String id);
    List<Staff> findAll();
    List<Staff> match();

    List<Staff> match(Criteria criteria);

    void deleteById(String id);
    Staff findByUserId(String participantId);
}
