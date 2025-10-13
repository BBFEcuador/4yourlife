package com.foryourlife.shared.domain.user.applications;

import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.user.User;
import com.foryourlife.shared.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandGeneralUserService {
    private final UserRepository repository;

    public CommandGeneralUserService(UserRepository repository) {
        this.repository = repository;
    }

    public void save(User user){
        repository.findByEmail(user.getEmail()).ifPresent(user1 -> {
            throw new BaseException("Usuario ya registrado", List.of("El email "+user.getEmail()+" ya fue registrado"));
        });
        repository.save(user);
    }
}
