package com.foryourlife.staff.application;

import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import org.springframework.stereotype.Service;

@Service
public class StaffCreatorService {
    private final StaffRepository _repository;

    public StaffCreatorService(StaffRepository _repository) {
        this._repository = _repository;
    }

    public void create(Staff staff){
        _repository.save(staff);
    }

    public void delete(String id){
        _repository.deleteById(id);
    }

}
