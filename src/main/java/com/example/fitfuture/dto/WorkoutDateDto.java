package com.example.fitfuture.dto;

import java.time.LocalDate;

public class WorkoutDateDto {
    private LocalDate date;
    private String athleteUsername;

    // Costruttore DTO
    public WorkoutDateDto(LocalDate date, String athleteUsername) {
        this.date = date;
        this.athleteUsername = athleteUsername;
    }

    // Getters & Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAthleteUsername() {
        return athleteUsername;
    }

    public void setAthleteUsername(String athleteUsername) {
        this.athleteUsername = athleteUsername;
    }
}
