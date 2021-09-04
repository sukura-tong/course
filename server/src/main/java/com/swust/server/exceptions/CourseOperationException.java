package com.swust.server.exceptions;

public class CourseOperationException extends RuntimeException{

    private static final long serialVersionUID = 2816539761879540211L;

    public CourseOperationException(String message) {
        super(message);
    }
}
