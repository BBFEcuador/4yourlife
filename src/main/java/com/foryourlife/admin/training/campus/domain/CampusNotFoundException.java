package com.foryourlife.admin.training.campus.domain;

public class CampusNotFoundException extends RuntimeException {
    public CampusNotFoundException(String message) {
        super(message);
    }
}
