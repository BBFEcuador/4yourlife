package com.foryourlife.staff.staffUser.domain;

import java.util.Optional;

public interface StaffRepository {
    void save(Staff staff);
    Optional<Staff> findById(String id);
    void deleteById(String id);
    Staff findByUserId(String participantId);
}
