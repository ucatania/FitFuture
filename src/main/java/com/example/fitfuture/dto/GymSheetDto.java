package com.example.fitfuture.dto;

import java.util.List;

public class GymSheetDto {
    private String athleteId;
    private String personalTrainerId;
    private List<String> exerciseIds;  // List of exercise IDs

    // costruttore DTO
    public GymSheetDto(String athleteId, String personalTrainerId, List<String> exerciseIds) {
        this.athleteId = athleteId;
        this.personalTrainerId = personalTrainerId;
        this.exerciseIds = exerciseIds;
    }

    public GymSheetDto(){
    }

    // Getters & Setters
    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getPersonalTrainerId() {
        return personalTrainerId;
    }

    public void setPersonalTrainerId(String personalTrainerId) {
        this.personalTrainerId = personalTrainerId;
    }

    public List<String> getExerciseIds() {
        return exerciseIds;
    }

    public void setExerciseIds(List<String> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }
}
