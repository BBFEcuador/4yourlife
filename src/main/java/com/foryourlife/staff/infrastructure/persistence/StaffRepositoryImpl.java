package com.foryourlife.staff.infrastructure.persistence;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.infrastructure.criteria.JPACriteriaConverter;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StaffRepositoryImpl implements StaffRepository {
    private static JpaStaffRepository repository;
    private static JPACriteriaConverter<Staff> criteriaConverter;

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
    public List<Staff> findAvailableStaff(LocalDate startDate, LocalDate endDate) {
        return repository.findAvailableStaff(startDate, endDate);
    }

    @Override
    public List<Staff> match(Criteria criteria) {
        return repository.findAll(criteriaConverter.getJpaSpecifications(criteria));
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Staff findByUserId(String userId) {
        return repository.findByUser_Id(userId);
    }

    @Override
    public boolean isStaffAvailable(String staffId, LocalDate startDate, LocalDate endDate, String newTrainingId) {
        return repository.isStaffAvailable(staffId, startDate, endDate, newTrainingId);
    }

}
