package com.example.parking.exceptions;

public class VisitNotFoundException extends Exception {

    public VisitNotFoundException(Long id){super("Unable to find visit with id = " + id);}
    public VisitNotFoundException(String registrationPlate){super("Unable to find visit with registration plate" +
                                                                    " = " + registrationPlate);}
}
