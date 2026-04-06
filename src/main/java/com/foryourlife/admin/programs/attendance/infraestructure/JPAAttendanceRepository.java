package com.foryourlife.admin.programs.attendance.infraestructure;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.training.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface JPAAttendanceRepository extends JpaRepository<Attendance,String> {
    List<Attendance> findByTraining_Id(String trainingId);
    List<Attendance> findByUser_Id(String userId);

    List<Attendance> findAllByUser_IdAndTrainingIn(String userId, Collection<Training> trainings);

    @Query("""
    SELECT a FROM Attendance a
    WHERE a.user.id IN :userIds
""")
    List<Attendance> findAllByUser_Ids(List<String> userIds);

    List<Attendance> findAllByTraining_NumberAndTraining_Campus_IdAndUser_IdIn(Integer trainingNumber, String trainingCampusId, Collection<String> userIds);
}
