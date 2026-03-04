package com.foryourlife.staff.application;

import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        return _repository.findByUserId(userId).orElseThrow(() ->
                new BaseException("Error", List.of("Staff no encontrado por usuairo"))
        );
    }

    public List<Staff> getAll() {
        return _repository.findAll();
    }
    public Page<Staff> getAll(Pageable pageable) {
        return _repository.findAll(pageable);
    }
    public Page<Staff> getAll(Pageable pageable, Criteria criteria) {
        return _repository.findAll(pageable,criteria);
    }

    public List<Staff> match(Criteria criteria) {
        return _repository.match(criteria);
    }

    public List<Staff> findAvailableStaff(LocalDate startDate, LocalDate endDate) {
        return _repository.findAvailableStaff(startDate, endDate);
    }


}
