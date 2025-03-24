package com.foryourlife.staff.application;

import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.shared.domain.user.applications.CommandGeneralUserService;
import com.foryourlife.staff.domain.Staff;
import com.foryourlife.staff.domain.StaffRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffCreatorService {
    private final StaffRepository _repository;
    private final UserRepository _userRepository;
    private final CommandGeneralUserService userRepositoryCreator;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository _adminRepository;

    public StaffCreatorService(StaffRepository _repository, UserRepository userRepository, CommandGeneralUserService userRepositoryCreator, PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this._repository = _repository;
        this._userRepository = userRepository;
        this.userRepositoryCreator = userRepositoryCreator;
        this.passwordEncoder = passwordEncoder;
        this._adminRepository = adminRepository;
    }

    public void create(Staff staff){
        var user = staff.getUser();
        if (user.getPassword() == null){
            user.setPassword(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepositoryCreator.save(user);
        if (staff.getActive() == null)
            staff.setActive(true);
        _repository.save(staff);
    }

    public void changeStatus(String visionaryId){
        var staff = _repository.findById(visionaryId).orElseThrow(() -> new BaseException("Not Found", List.of()));
        staff.changeStatus();
        _repository.save(staff);
    }

    public void createFromAdmin(Staff staff) {
        if (_repository.findByUserId(staff.getUser().getId()) != null) {
            throw new BaseException("The user is already a Staff", List.of("Already exist as staff"));
        }

        var admin = _adminRepository.findByUserId(staff.getUser().getId()).orElseThrow(() ->
                new BaseException("User not found", List.of("User does not exist"))
        );

        var user = admin.getUser();
        user.getEntityMap().add(new UserEntities(staff.getId(), UserType.STAFF.name()));
        _userRepository.save(user);
        _repository.save(staff);
    }

    public void createFromParticipant(Staff staff) {
        if (_repository.findByUserId(staff.getUser().getId()) != null) {
            throw new BaseException("The user is already a Staff", List.of("Already exist as staff"));
        }

        var user = _userRepository.findById(staff.getUser().getId()).orElseThrow(() ->
                new BaseException("User not found", List.of("User does not exist"))
        );

        user.getEntityMap().add(new UserEntities(staff.getId(), UserType.STAFF.name()));
        _userRepository.save(user);
        _repository.save(staff);
    }
}
