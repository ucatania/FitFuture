package com.example.fitfuture.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.fitfuture.repository.GymSheetRepository;
import com.example.fitfuture.dto.GymSheetDto;
import com.example.fitfuture.entity.GymSheet;

@Service
public class GymSheetService {
    private final GymSheetRepository gymSheetRepository;

    @Autowired
    public GymSheetService(GymSheetRepository gymSheetRepository) {
        this.gymSheetRepository = gymSheetRepository;
    }

    // Method to add a new gym sheet
    public void addGymSheet(GymSheetDto gymSheetDto) {
        GymSheet gymSheet = new GymSheet(null, gymSheetDto.getAthleteId(),
                gymSheetDto.getPersonalTrainerId(),
                gymSheetDto.getExerciseIds());
        gymSheetRepository.save(gymSheet);
    }

    // Method to retrieve all gym sheets
    public List<GymSheet> getAllGymSheets() {
        return gymSheetRepository.findAll();
    }

    // Method to retrieve gym sheets for a specific athlete
    public List<GymSheet> getGymSheetsByAthlete(String athleteId) {
        return gymSheetRepository.findByAthleteId(athleteId);
    }

    public List<GymSheet> getGymSheetsByTrainer(String trainerId) {
        return gymSheetRepository.findByPersonalTrainerId(trainerId);
    }

    // Method to update a gym sheet
    public void updateGymSheet(String gymSheetId, GymSheetDto gymSheetDto) {
        GymSheet gymSheet = new GymSheet(gymSheetId, gymSheetDto.getAthleteId(),
                gymSheetDto.getPersonalTrainerId(),
                gymSheetDto.getExerciseIds());
        gymSheetRepository.save(gymSheet);
    }

    // Method to delete a gym sheet
    public void deleteGymSheet(String gymSheetId) {
        gymSheetRepository.deleteById(gymSheetId);
    }
}
