package com.foryourlife.visionary.application;

import com.foryourlife.admin.auth.domain.AdminRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.shared.domain.user.applications.CommandGeneralUserService;
import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VisionaryCreatorService {
    private final VisionaryRepository repository;
    private final AdminRepository _adminRepository;
    private final UserRepository userRepository;
    private final CommandGeneralUserService userRepositoryCreator;
    private final PasswordEncoder passwordEncoder;

    public VisionaryCreatorService(VisionaryRepository repository, AdminRepository adminRepository, UserRepository userRepository, CommandGeneralUserService userRepositoryCreator, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        _adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.userRepositoryCreator = userRepositoryCreator;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(Visionary visionary) {
        var user = visionary.getUser();
        if (user.getPassword() == null) {
            user.setPassword(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepository.findById(user.getId()).isPresent()) {
            userRepository.save(user);
        } else {
            userRepositoryCreator.save(user);
        }
        if (visionary.getActive() == null)
            visionary.setActive(true);
        repository.save(visionary);
    }

    public void changeStatus(String visionaryId) {
        var visionary = repository.findById(visionaryId).orElseThrow(() -> new BaseException("No encontrado", List.of()));
        visionary.changeStatus();
        repository.save(visionary);
    }

    public void createFromAdmin(Visionary visionary) {
        if (repository.findByUserId(visionary.getUser().getId()).isPresent()) {
            throw new BaseException("El usuario ya es un Visionario", List.of("Ya es un Visionario"));
        }

        var admin = _adminRepository.findByUserId(visionary.getUser().getId()).orElseThrow(() ->
                new BaseException("Usuario no encontrado", List.of("El usuario no existe"))
        );

        var user = admin.getUser();
        user.getEntityMap().add(new UserEntities(visionary.getId(), UserType.STAFF.name()));
        userRepository.save(user);
        repository.save(visionary);
    }

    public void createFromParticipant(String userId, String role) {
        if (repository.findByUserId(userId).isPresent()) {
            throw new BaseException("El usuario ya es un Visionario", List.of("Ya es un Visionario"));
        }

        var user = userRepository.findById(userId).orElseThrow(() ->
                new BaseException("Usuario no encontrado", List.of("El usuario no existe"))
        );

        var visionary = Visionary.create(userId, role, true, user);
        user.getEntityMap().add(new UserEntities(visionary.getId(), UserType.VISIONARY.name()));
        userRepository.save(user);
        repository.save(visionary);
    }
}
