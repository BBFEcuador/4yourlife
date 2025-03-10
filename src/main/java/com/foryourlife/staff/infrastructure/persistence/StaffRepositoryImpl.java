package com.foryourlife.staff.infrastructure.persistence;

import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffRepositoryImpl implements StaffRepository {
    private static JpaStaffRepository repository;

    public StaffRepositoryImpl(JpaStaffRepository repository) {
        StaffRepositoryImpl.repository = repository;
    }

    @Override
    public void save(Staff staff) {
        repository.save(staff);
    }

    @Override
    public Optional<Staff> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Staff> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Staff findByUserId(String userId) {
        return repository.findByUser_Id(userId);
    }
}
