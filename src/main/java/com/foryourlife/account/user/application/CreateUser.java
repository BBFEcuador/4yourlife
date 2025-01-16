package com.foryourlife.account.user.application;

import com.foryourlife.account.user.domain.UserAlreadyCreatedException;
import com.foryourlife.account.user.domain.UserRepository;
import com.foryourlife.account.user.domain.Users;
import com.foryourlife.shared.domain.bus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateUser {
    private final UserRepository _userRepository;
    private final EventBus bus;
    private final Logger logger = LoggerFactory.getLogger(CreateUser.class);

    public CreateUser(UserRepository _userRepository, EventBus bus) {
        this._userRepository = _userRepository;
        this.bus = bus;
    }

    public void save (Users user){
        if(this._userRepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyCreatedException("The email " + user.getEmail() + "is already registered");
        try {
            this._userRepository.save(user);
            this.bus.publish(user.pullDomainEvents());
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }
}
