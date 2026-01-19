package com.example.demo.exception;

public class BookAlreadyIssuedException extends RuntimeException{
    public BookAlreadyIssuedException(String message){
        super(message);
    }
}
