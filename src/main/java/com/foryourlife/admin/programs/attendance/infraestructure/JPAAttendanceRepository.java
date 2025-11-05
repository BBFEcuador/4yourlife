package com.foryourlife.admin.programs.attendance.infraestructure;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPAAttendanceRepository extends JpaRepository<Attendance,String> {
    List<Attendance> findByTraining_Id(String trainingId);
    List<Attendance> findByUser_Id(String userId);
}
