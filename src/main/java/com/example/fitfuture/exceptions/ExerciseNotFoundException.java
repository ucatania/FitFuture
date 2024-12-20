package com.example.fitfuture.exceptions;

public class ExerciseNotFoundException extends RuntimeException {

    public ExerciseNotFoundException(String message) {
        super(message);
    }

    public ExerciseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExerciseNotFoundException(Long exerciseId) {
        super("Exercise not found with ID: " + exerciseId);
    }
}
