package com.foryourlife.staff.staffUser.infrastructure.persistence;

import com.foryourlife.staff.staffUser.domain.Staff;
import com.foryourlife.staff.staffUser.domain.StaffRepository;
import org.springframework.stereotype.Service;

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
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Staff findByUserId(String userId) {
        return repository.findByUser_Id(userId);
    }
}
