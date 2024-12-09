package com.serdarormanli.backbase.controller;

import com.serdarormanli.backbase.omdb.OMDBApiErrorException;
import com.serdarormanli.backbase.omdb.OMDBApiStatusCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;

@Component
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors().stream().
                map(fieldError -> "%s %s".formatted(fieldError.getField(), fieldError.getDefaultMessage())).
                toList();

        return new ResponseEntity<>(new Error(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OMDBApiErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Error> handleOMDBApiErrorException(OMDBApiErrorException e) {
        return new ResponseEntity<>(new Error(Collections.singletonList(e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OMDBApiStatusCodeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<Error> handleOMDBApiStatusCodeException(OMDBApiStatusCodeException e) {
        return new ResponseEntity<>(new Error(Collections.singletonList(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Error> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return new ResponseEntity<>(new Error(Collections.singletonList("Duplicate request")), HttpStatus.BAD_REQUEST);
    }

    private record Error(List<String> errors) {
    }
}
