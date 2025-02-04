package com.example.fitfuture.exceptions;

public class ExerciseAlreadyExistingException extends RuntimeException {

    public ExerciseAlreadyExistingException(String message) {
        super(message);
    }

    public ExerciseAlreadyExistingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExerciseAlreadyExistingException(Long exerciseId) {
        super("This exercise already exists!" + exerciseId);
    }
}

