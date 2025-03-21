package com.foryourlife.masterLife.application;

import com.foryourlife.masterLife.domain.MasterLife;
import com.foryourlife.masterLife.domain.MasterLifeRepository;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.applications.CommandGeneralUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommandMasterLifeService {

    private final MasterLifeRepository repository;
    private final CommandGeneralUserService userRepositoryCreator;
    private final PasswordEncoder passwordEncoder;

    public CommandMasterLifeService(MasterLifeRepository repository, CommandGeneralUserService userRepositoryCreator, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepositoryCreator = userRepositoryCreator;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void save(MasterLife masterLife){
        var user = masterLife.getUser();
        if (user.getPassword() == null){
            user.setPassword(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepositoryCreator.save(user);
        repository.save(masterLife);
    }

    public void changeStatus(String visionaryId){
        var masterLife = repository.findById(visionaryId).orElseThrow(() -> new BaseException("Not Found", List.of()));
        masterLife.changeStatus();
        repository.save(masterLife);
    }

}
