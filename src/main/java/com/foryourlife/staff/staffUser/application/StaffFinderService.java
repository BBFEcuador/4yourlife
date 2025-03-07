package com.foryourlife.staff.staffUser.application;

import com.foryourlife.staff.staffUser.domain.Staff;
import com.foryourlife.staff.staffUser.domain.StaffRepository;
import org.springframework.stereotype.Service;

@Service
public class StaffFinderService {
    private final StaffRepository _repository;

    public StaffFinderService(StaffRepository repository){
        _repository = repository;
    }

    public Staff findById(String id){
        return _repository.findById(id).orElse(null);
    }

    public Staff findByUserId(String userId){
        return _repository.findByUserId(userId);
    }
}
