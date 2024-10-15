package com.example.fitfuture.dto;
import java.util.Date;

public class WorkoutDto {
    private String athleteId; // ID dell'atleta
    private String gymSheetId; // ID della GymSheet
    private Date date; // Data dell'allenamento

    // Costruttore
    public WorkoutDto(String athleteId, String gymSheetId, Date date) {
        this.athleteId = athleteId;
        this.gymSheetId = gymSheetId;
        this.date = date;
    }

    public WorkoutDto() {

    }

    // Getters e Setters
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
