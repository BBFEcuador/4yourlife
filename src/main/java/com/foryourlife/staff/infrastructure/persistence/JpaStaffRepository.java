package com.foryourlife.staff.infrastructure.persistence;

import com.foryourlife.admin.programs.trainer.domain.Trainer;
import com.foryourlife.staff.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaStaffRepository extends JpaRepository<Staff, String>, JpaSpecificationExecutor<Staff> {
    Staff findByUser_Id(String userId);
}
