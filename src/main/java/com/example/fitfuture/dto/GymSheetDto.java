    package com.example.fitfuture.dto;
    import com.fasterxml.jackson.annotation.JsonProperty;

    import java.util.List;

    public class GymSheetDto {
        private String athleteId;
        private String personalTrainerId;

        @JsonProperty("exerciseIds")
        private List<String> exerciseIds;

        public GymSheetDto(String athleteId, String personalTrainerId, List<String> exerciseIds) {
            this.athleteId = athleteId;
            this.personalTrainerId = personalTrainerId;
            this.exerciseIds = exerciseIds;
        }

        public GymSheetDto(){
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
