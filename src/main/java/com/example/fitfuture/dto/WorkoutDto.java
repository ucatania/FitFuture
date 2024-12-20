package com.example.fitfuture.dto;

import java.time.LocalDate;

public class WorkoutDto {
    private String athleteId;
    private String gymSheetId;
    private LocalDate date;
    private String notes;

    // Costruttori DTO
    public WorkoutDto(String athleteId, String gymSheetId, LocalDate date, String notes) {
        this.athleteId = athleteId;
        this.gymSheetId = gymSheetId;
        this.date = date;
        this.notes = notes;
    }

    public WorkoutDto(String athleteId, String gymSheetId, LocalDate date) {
        this.athleteId = athleteId;
        this.gymSheetId = gymSheetId;
        this.date = date;
        this.notes = null;
    }

    public WorkoutDto() {
    }

    // Getters & Setters
    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getGymSheetId() {
        return gymSheetId;
    }

    public void setGymSheetId(String gymSheetId) {
        this.gymSheetId = gymSheetId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
