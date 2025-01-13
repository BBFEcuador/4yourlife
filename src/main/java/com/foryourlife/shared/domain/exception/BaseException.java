package com.foryourlife.shared.domain.exception;

import java.util.List;

public class BaseException extends Exception{
    private List<String> errors;
    public BaseException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
