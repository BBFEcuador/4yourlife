package com.foryourlife.admin.programs.attendance.infraestructure;

import com.foryourlife.admin.programs.attendance.domain.Attendance;
import com.foryourlife.admin.programs.attendance.domain.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final JPAAttendanceRepository _jpaAttendanceRepository;

    public AttendanceRepositoryImpl(JPAAttendanceRepository _jpaAttendanceRepository) {
        this._jpaAttendanceRepository = _jpaAttendanceRepository;
    }

    @Override
    public void save(Attendance attendance) {
        _jpaAttendanceRepository.save(attendance);
    }

    @Override
    public void saveAll(List<Attendance> attendances) {
        _jpaAttendanceRepository.saveAll(attendances);
    }

    @Override
    public Optional<Attendance> findById(String id) {
        return _jpaAttendanceRepository.findById(id);
    }

    @Override
    public List<Attendance> findAttendanceByUser(String userId) {
        return _jpaAttendanceRepository.findByUser_Id(userId);
    }

    @Override
    public List<Attendance> findAttendanceByTraining(String trainingId) {
        return _jpaAttendanceRepository.findByTraining_Id(trainingId);
    }
}
