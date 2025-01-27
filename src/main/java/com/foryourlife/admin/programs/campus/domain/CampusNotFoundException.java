package com.foryourlife.admin.programs.campus.domain;

public class CampusNotFoundException extends RuntimeException {
    public CampusNotFoundException(String message) {
        super(message);
    }
}
