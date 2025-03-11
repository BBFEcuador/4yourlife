package com.foryourlife.visionary.application;

import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisionaryCreatorService {
    private static VisionaryRepository _repository;
    private final UserRepository _userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository _adminRepository;

    public VisionaryCreatorService(VisionaryRepository _repository, UserRepository userRepository, PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this._repository = _repository;
        this._userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this._adminRepository = adminRepository;
    }

    public void create(Visionary visionary){
        var user = visionary.getUser();
        if (user.getPassword() == null){
            user.setPassword(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        _userRepository.save(user);
        _repository.save(visionary);
    }


    public void delete(String visionaryId){
        _repository.deleteById(visionaryId);
    }

    public void createFromAdmin(Visionary visionary) {
        if (_repository.findByUserId(visionary.getUser().getId()) != null) {
            throw new BaseException("The user is already a Visionary", List.of("Already exist as visionary"));
        }

        var admin = _adminRepository.findByUserId(visionary.getUser().getId()).orElseThrow(() ->
                new BaseException("User not found", List.of("User does not exist"))
        );

        var user = admin.getUser();
        user.getEntityMap().add(new UserEntities(visionary.getId(), UserType.STAFF.name()));
        _userRepository.save(user);
        _repository.save(visionary);
    }
}
