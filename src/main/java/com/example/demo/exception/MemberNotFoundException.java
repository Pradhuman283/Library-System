package com.example.demo.exception;

public class MemberNotFoundException extends RuntimeException{
public MemberNotFoundException(String Message){
    super(Message);
}
}
