package com.foryourlife.masterLife.application;

import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.masterLife.domain.MasterLifeRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.UserEntities;
import com.foryourlife.shared.domain.user.UserRepository;
import com.foryourlife.shared.domain.user.UserType;
import com.foryourlife.shared.domain.user.applications.CommandGeneralUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CommandMasterLifeService {

    private final MasterLifeRepository repository;
    private final UserRepository userRepository;
    private final CommandGeneralUserService userRepositoryCreator;
    private final PasswordEncoder passwordEncoder;

    public CommandMasterLifeService(MasterLifeRepository repository, UserRepository userRepository, CommandGeneralUserService userRepositoryCreator, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.userRepositoryCreator = userRepositoryCreator;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void save(MasterLife masterLife) {
        var user = masterLife.getUser();
        if (user.getPassword() == null) {
            user.setPassword(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepositoryCreator.save(user);
        repository.save(masterLife);
    }

    @Transactional
    public void saveFromParticipant(String id) {
        repository.findByUserId(id).ifPresent(masterLife -> {
            throw new BaseException("Masterlife already created with the user id", List.of());
        });
        var user = userRepository.findById(id).orElseThrow(
                () -> new BaseException("User not found", List.of())
        );
        var masterLifeId = UUID.randomUUID().toString();
        var masterLife = MasterLife.create(
                masterLifeId,
                true,
                user
        );
        user.getEntityMap().add(new UserEntities(masterLifeId, UserType.MASTER_LIFE.name()));
        userRepositoryCreator.save(user);
        repository.save(masterLife);
    }

    public void changeStatus(String visionaryId) {
        var masterLife = repository.findById(visionaryId).orElseThrow(() -> new BaseException("Not Found", List.of()));
        masterLife.changeStatus();
        repository.save(masterLife);
    }

}
