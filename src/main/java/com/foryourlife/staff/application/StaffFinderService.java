package com.foryourlife.staff.application;

import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
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
