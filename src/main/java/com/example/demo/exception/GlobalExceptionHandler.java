package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookException(BookNotFoundException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );

    }
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberException(MemberNotFoundException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(BookAlreadyIssuedException.class)
    public ResponseEntity<String> handleBookAlreadyIssuedException(BookAlreadyIssuedException ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAlreadyRegistredException(AlreadyRegistredForBookException ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyException(Exception ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
        }

    }





