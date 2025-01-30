package com.foryourlife.clients.account.user.application;

import com.foryourlife.clients.account.user.domain.UserNotFoundException;
import com.foryourlife.clients.account.user.domain.UserRepository;
import com.foryourlife.clients.account.user.domain.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryUsersService {
    private final UserRepository _userRepository;
    private final Logger logger = LoggerFactory.getLogger(QueryUsersService.class);

    public QueryUsersService(UserRepository _userRepository) {
        this._userRepository = _userRepository;
    }


    public Users getUserById(String id) {
        return this._userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("The Id: " + id + " doesn't exist."));
    }

    public List<Users> getAll() {
        return this._userRepository.getAll();
    }
}
