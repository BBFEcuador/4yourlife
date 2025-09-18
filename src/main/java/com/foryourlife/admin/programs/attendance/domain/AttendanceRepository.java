package com.foryourlife.admin.programs.attendance.domain;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
    void save(Attendance attendance);

    void saveAll(List<Attendance> attendances);

    Optional<Attendance> findById(String id);

    List<Attendance> findAttendanceByUser(String userId);

    List<Attendance> findAttendanceByTraining(String trainingId);
}
