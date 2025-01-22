package com.foryourlife.clients.account.user.domain;

public class UserAlreadyCreatedException extends RuntimeException{
    public UserAlreadyCreatedException(String message) {
        super(message);
    }
}
