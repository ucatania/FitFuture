package com.example.fitfuture.entity;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "gymSheets")
public class GymSheet {
    @Id
    private String id;
    private String athleteId;
    private String personalTrainerId;
    private List<String> exerciseIds;

    public GymSheet(String id, String athleteId, String personalTrainerId, List<String> exerciseIds) {
        this.id = id;
        this.athleteId = athleteId;
        this.personalTrainerId = personalTrainerId;
        this.exerciseIds = exerciseIds;
    }

    // Getters and Setters
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
