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
            throw new BaseException("Email taken", List.of("The email "+user.getEmail()+" is already taken"));
        });
        repository.save(user);
    }
}
