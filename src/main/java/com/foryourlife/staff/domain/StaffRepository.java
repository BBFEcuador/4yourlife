package com.foryourlife.staff.domain;

import com.foryourlife.shared.domain.criteria.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StaffRepository {
    void save(Staff staff);
    Optional<Staff> findById(String id);
    List<Staff> findAll();
    Page<Staff> findAll(Pageable pageable);
    Page<Staff> findAll(Pageable pageable, Criteria criteria);
    List<Staff> findAvailableStaff(LocalDate startDate,LocalDate endDate);

    List<Staff> match(Criteria criteria);

    void deleteById(String id);
    Optional<Staff> findByUserId(String participantId);
    boolean isStaffAvailable(String staffId, LocalDate startDate, LocalDate endDate, String newTrainingId);
}
