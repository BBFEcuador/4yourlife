package com.foryourlife.staff.infrastructure.persistence;

import com.foryourlife.staff.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStaffRepository extends JpaRepository<Staff, String> {
    Staff findByUser_Id(String userId);
}
