package com.foryourlife.staff.application;

import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StaffFinderService {
    private final StaffRepository _repository;

    public StaffFinderService(StaffRepository _repository) {
        this._repository = _repository;
    }

    public Staff findById(String id) {
        return _repository.findById(id).orElse(null);
    }

    public Staff findByUserId(String userId) {
        return _repository.findByUserId(userId);
    }

    public List<Staff> getAll() {
        return _repository.findAll();
    }

    public List<Staff> match(Criteria criteria) {
        return _repository.match(criteria);
    }

    public List<Staff> findAvailableStaff(LocalDate startDate, LocalDate endDate) {
        return _repository.findAvailableStaff(startDate, endDate);
    }


}
