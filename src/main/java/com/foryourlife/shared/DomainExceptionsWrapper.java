package com.foryourlife.shared;

import java.util.List;

public class DomainExceptionsWrapper {

    private String message;

    private List<String> errors;

    public DomainExceptionsWrapper(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }
}
