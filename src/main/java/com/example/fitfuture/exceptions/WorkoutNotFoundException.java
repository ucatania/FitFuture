package com.example.fitfuture.exceptions;

public class WorkoutNotFoundException extends RuntimeException {

    public WorkoutNotFoundException(String message) {
        super(message);
    }

    public WorkoutNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkoutNotFoundException(Long workoutId) {
        super("Workout not found with ID: " + workoutId);
    }
}
