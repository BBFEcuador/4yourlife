package com.foryourlife.staff.application;

import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StaffCreatorService {
    private final StaffRepository _repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffCreatorService(StaffRepository _repository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this._repository = _repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(Staff staff){
        var user = staff.getUser();
        if (user.getPassword() == null){
            user.setPassword(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        _repository.save(staff);
    }

    public void delete(String id){
        _repository.deleteById(id);
    }

}
