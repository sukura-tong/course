package com.swust.server.exceptions;

public class ValidatorException extends RuntimeException{

    private static final long serialVersionUID = -3720268303939306705L;

    public ValidatorException(String message) {
        super(message);
    }
}
