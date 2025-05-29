package com.foryourlife.clients.account.participant.domain;

public class UserAlreadyCreatedException extends RuntimeException{
    public UserAlreadyCreatedException(String message) {
        super(message);
    }
}
