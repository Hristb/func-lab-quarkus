package com.lab.domain.exception;

public class InvalidProcessingRequestException extends RuntimeException {

    public InvalidProcessingRequestException(String message) {
        super(message);
    }
}
