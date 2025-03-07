package com.foryourlife.staff.staffUser.infrastructure.persistence;

import com.foryourlife.staff.staffUser.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStaffRepository extends JpaRepository<Staff, String> {
    Staff findByUser_Id(String userId);
}
