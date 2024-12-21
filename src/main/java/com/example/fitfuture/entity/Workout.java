package com.example.fitfuture.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "workouts")
public class Workout {
    @Id
    private String id;
    private String athleteId;
    private String gymSheetId;
    private String notes;
    private LocalDate date;

    public Workout(String athleteId, String gymSheetId, LocalDate date, String notes) {
        this.athleteId = athleteId;
        this.gymSheetId = gymSheetId;
        this.date = date;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setDate(LocalDate date) { this.date = date; }

    public static String getNotes() { return notes; }

    public void setNotes(String notes) {this.notes = notes;}
}
