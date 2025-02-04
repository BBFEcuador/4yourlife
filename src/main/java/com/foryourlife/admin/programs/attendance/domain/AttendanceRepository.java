package com.foryourlife.admin.programs.attendance.domain;

import java.util.List;

public interface AttendanceRepository {
    void save (Attendance attendance);
    void saveAll (List<Attendance> attendances);
    List<Attendance> findAttendanceByUser(String userId);
    List<Attendance> findAttendanceByTraining(String trainingId);
}
