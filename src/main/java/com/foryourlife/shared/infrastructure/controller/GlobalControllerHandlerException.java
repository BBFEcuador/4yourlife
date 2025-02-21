package com.foryourlife.shared.infrastructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryourlife.admin.programs.campus.domain.CampusNotFoundException;
import com.foryourlife.clients.account.user.domain.UserAlreadyCreatedException;
import com.foryourlife.clients.account.user.domain.UserNotFoundException;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.exception.DomainExceptionsWrapper;
import jakarta.annotation.Nullable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerHandlerException extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @Nullable HttpHeaders headers,
            @Nullable HttpStatusCode status,
            @Nullable WebRequest request
    ) {
        List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        System.out.println(errorList.size());
        DomainExceptionsWrapper errors = new DomainExceptionsWrapper("Form validator errors", errorList);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var error = ex.getCause().getMessage();
        System.out.println("=============================================");
        System.out.println(error);
        System.out.println("=============================================");
        DomainExceptionsWrapper errors = new DomainExceptionsWrapper(ex.getMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException ex) {
        DomainExceptionsWrapper errors = new DomainExceptionsWrapper(ex.getMessage(), ex.getErrors());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyCreatedException.class)
    public ResponseEntity<Object> handleUserAlreadyCreatedException(UserAlreadyCreatedException ex) {
        DomainExceptionsWrapper errors = new DomainExceptionsWrapper(ex.getMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        DomainExceptionsWrapper errors = new DomainExceptionsWrapper(ex.getMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        DomainExceptionsWrapper errors = new DomainExceptionsWrapper(ex.getMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CampusNotFoundException.class)
    public ResponseEntity<Object> handleCampusNotFoundException(CampusNotFoundException ex) {
        DomainExceptionsWrapper errors = new DomainExceptionsWrapper(ex.getMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        DomainExceptionsWrapper errors = new DomainExceptionsWrapper(ex.getMessage(), List.of(ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
