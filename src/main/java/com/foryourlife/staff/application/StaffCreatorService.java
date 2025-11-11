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

    public void create(Staff staff) {
        var user = staff.getUser();
        if (user.getPassword() == null) {
            user.setPassword(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (_userRepository.findById(user.getId()).isPresent()) {
            _userRepository.save(user);
        } else {
            userRepositoryCreator.save(user);
        }
        if (staff.getActive() == null)
            staff.setActive(true);
        _repository.save(staff);
    }

    public void update(Staff staff) {
        var user = staff.getUser();
        if (user.getPassword() == null) {
            user.setPassword(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        _userRepository.save(user);
        if (staff.getActive() == null)
            staff.setActive(true);
        _repository.save(staff);
    }

    public void changeStatus(String visionaryId) {
        var staff = _repository.findById(visionaryId).orElseThrow(() -> new BaseException("No encontrado", List.of()));
        staff.changeStatus();
        _repository.save(staff);
    }

    public void createFromAdmin(Staff staff) {
        if (_repository.findByUserId(staff.getUser().getId()) != null) {
            throw new BaseException("El usuario ya es Staff", List.of("Ya es un Staff"));
        }

        var admin = _adminRepository.findByUserId(staff.getUser().getId()).orElseThrow(() ->
                new BaseException("Usuario no encontrado", List.of("El usuario no existe"))
        );

        var user = admin.getUser();
        user.getEntityMap().add(new UserEntities(staff.getId(), UserType.STAFF.name()));
        _userRepository.save(user);
        _repository.save(staff);
    }

    public void createFromParticipant(String userId, String role) {
        if (role.isEmpty()){
            throw new BaseException("El rol no puede estar vacío", List.of("El rol es obligatorio"));
        }
        if (_repository.findByUserId(userId) != null) {
            throw new BaseException("El usuario ya es Staff", List.of("Ya es un Staff"));
        }

        var user = _userRepository.findById(userId).orElseThrow(() ->
                new BaseException("Usuario no encontrado", List.of("El usuario no existe"))
        );

        var staff = Staff.create(userId, role, true, user);

        user.getEntityMap().add(new UserEntities(staff.getId(), UserType.STAFF.name()));
        _userRepository.save(user);
        _repository.save(staff);
    }
}
