package com.foryourlife.staff.domain;

import java.util.List;
import java.util.Optional;

public interface StaffRepository {
    void save(Staff staff);
    Optional<Staff> findById(String id);
    List<Staff> findAll();
    void deleteById(String id);
    Staff findByUserId(String participantId);
}
