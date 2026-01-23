package com.example.demo.exception;

import org.apache.logging.log4j.message.Message;

public class AlreadyRegistredForBookException extends RuntimeException{
    public AlreadyRegistredForBookException(String message){
        super(message);
    }

}
