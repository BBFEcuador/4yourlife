package com.foryourlife.staff.staffUser.domain;

public interface StaffRepository {
    void save(Staff staff);
    Staff findById(String id);
    void deleteById(String id);
    Staff findByParticipantId(String participantId);

}
