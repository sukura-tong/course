package com.swust.server.exceptions;

public class ChapterOperationException extends RuntimeException{

    private static final long serialVersionUID = -7435509284571759253L;

    public ChapterOperationException(String message) {
        super(message);
    }
}
