package com.example.fitfuture.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "workouts")
public class Workout {
    @Id
    private String id;
    private String athleteId; // ID dell'atleta
    private String gymSheetId; // ID della GymSheet
    private Date date; // Data dell'allenamento

    // Costruttore
    public Workout(String athleteId, String gymSheetId, Date date) {
        this.athleteId = athleteId;
        this.gymSheetId = gymSheetId;
        this.date = date;
    }

    // Getters e Setters
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
