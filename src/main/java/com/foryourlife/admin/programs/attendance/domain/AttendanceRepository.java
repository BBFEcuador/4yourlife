package com.foryourlife.admin.programs.attendance.domain;

import com.foryourlife.admin.programs.training.domain.Training;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository {
    void save(Attendance attendance);

    void saveAll(List<Attendance> attendances);

    Optional<Attendance> findById(String id);

    List<Attendance> findAttendanceByUser(String userId);

    List<Attendance> findAttendanceByTraining(String trainingId);

    List<Attendance> findByUserIdAndTrainingIn(String userId, List<Training> trainingIds);

    List<Attendance> findAllByUserIds(List<String> userIds);

    List<Attendance> findAllByTrainingNumberAndUsersIdsAndCampusId(Integer trainingNumber, List<String> userId, String campusId);
}
