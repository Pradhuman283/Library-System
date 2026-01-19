package com.example.demo.exception;

public class BookNotFoundException extends RuntimeException {
public BookNotFoundException(String Message){
    super(Message);
}
}
