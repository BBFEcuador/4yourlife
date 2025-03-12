package com.foryourlife.visionary.application;

import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.visionary.domain.Visionary;
import com.foryourlife.visionary.domain.VisionaryRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VisionaryCreatorService {
    private final VisionaryRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public VisionaryCreatorService(VisionaryRepository repository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(Visionary visionary) {
        var user = visionary.getUser();
        if (user.getPassword() == null) {
            user.setPassword(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        if (visionary.getActive() == null)
            visionary.setActive(true);
        repository.save(visionary);
    }

    public void changeStatus(String visionaryId) {
        var visionary = repository.findById(visionaryId).orElseThrow(() -> new BaseException("Not Found", List.of()));
        visionary.changeStatus();
        repository.save(visionary);
    }
}
