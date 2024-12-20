package com.example.fitfuture.exceptions;

public class GymSheetNotFoundException extends RuntimeException {

    public GymSheetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GymSheetNotFoundException(Long gymSheetId) {
        super("Gym sheet not found with ID: " + gymSheetId);
    }

    public GymSheetNotFoundException(String gymSheetName) {
        super("Gym sheet not found with name: " + gymSheetName);
    }
}
