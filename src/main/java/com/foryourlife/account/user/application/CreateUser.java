package com.foryourlife.account.user.application;

import com.foryourlife.account.user.domain.UserRepository;
import com.foryourlife.account.user.domain.Users;
import com.foryourlife.shared.domain.bus.EventBus;
import org.springframework.stereotype.Service;

@Service
public class CreateUser {
    private final UserRepository _userRepository;
    private final EventBus bus;
    //Logger

    public CreateUser(UserRepository _userRepository, EventBus bus) {
        this._userRepository = _userRepository;
        this.bus = bus;
    }

    public void save (Users user){
        if(this._userRepository)
    }
}
