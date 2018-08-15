package com.example.parking.exceptions;

public class VisitCreationException extends BadRequestException {

    public VisitCreationException(String message) {
        super(message);
    }

    public VisitCreationException(String message, Throwable e) {
        super(message, e);
    }
}
