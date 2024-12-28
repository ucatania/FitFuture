package com.example.fitfuture.services;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.stream.Collectors;

import java.util.Optional;
import java.util.List;
import com.example.fitfuture.repository.*;
import com.example.fitfuture.dto.*;
import com.example.fitfuture.entity.*;

@Service
public class GymSheetService {
    private final GymSheetRepository gymSheetRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    public GymSheetService(UserRepository userRepository, ExerciseRepository exerciseRepository, GymSheetRepository gymSheetRepository) {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.gymSheetRepository = gymSheetRepository;
    }

    public void addGymSheet(GymSheetDto gymSheetDto) {

        Optional<User> athleteOpt = userRepository.findById(gymSheetDto.getAthleteId());
        if (athleteOpt.isEmpty() || !athleteOpt.get().getRole().equals(User.Role.ATLETA)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Atleta non trovato o ruolo non valido");
        }

        List<Exercise> exercises = exerciseRepository.findAllById(gymSheetDto.getExerciseIds());
        if (exercises.size() != gymSheetDto.getExerciseIds().size()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uno o più esercizi non trovati");
        }

        if (gymSheetDto.getPersonalTrainerId() != null) {
            Optional<User> personalTrainerOpt = userRepository.findById(gymSheetDto.getPersonalTrainerId());
            if (personalTrainerOpt.isEmpty() || !personalTrainerOpt.get().getRole().equals(User.Role.PERSONAL_TRAINER)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Personal Trainer non trovato o ruolo non valido");
            }

            GymSheet gymSheet = new GymSheet(null, gymSheetDto.getAthleteId(),
                    gymSheetDto.getPersonalTrainerId(),
                    gymSheetDto.getExerciseIds());
            gymSheetRepository.save(gymSheet);
        } else {
            GymSheet gymSheet = new GymSheet(null, gymSheetDto.getAthleteId(),
                    gymSheetDto.getExerciseIds());
            gymSheetRepository.save(gymSheet);
        }

    }

    public List<GymSheet> getAllGymSheets() {
        return gymSheetRepository.findAll();
    }

    public List<GymSheet> getGymSheetsByAthlete(String athleteId) {
        return gymSheetRepository.findByAthleteId(athleteId);
    }

    public List<GymSheet> getGymSheetsByTrainer(String trainerId) {
        return gymSheetRepository.findByPersonalTrainerId(trainerId);
    }

    public List<String> getAthletesByTrainerId(String trainerId) {
        List<GymSheet> gymSheets = gymSheetRepository.findByPersonalTrainerId(trainerId);

        List<String> athleteIds = gymSheets.stream()
                .map(GymSheet::getAthleteId)
                .distinct()
                .collect(Collectors.toList());

        List<User> athletes = userRepository.findByIdIn(athleteIds);

        return athletes.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        }

    public void updateGymSheet(String gymSheetId, GymSheetDto gymSheetDto) {
        GymSheet gymSheet = new GymSheet(gymSheetId, gymSheetDto.getAthleteId(),
                gymSheetDto.getPersonalTrainerId(),
                gymSheetDto.getExerciseIds());
        gymSheetRepository.save(gymSheet);
    }

    public void deleteGymSheet(String gymSheetId) {
        gymSheetRepository.deleteById(gymSheetId);
    }

    public List<GymSheet> getGymSheetsForAuthenticatedUser(String authenticatedUserId) {
        return gymSheetRepository.findAll().stream()
                .filter(gymSheet -> gymSheet.getAthleteId().equals(authenticatedUserId))
                .collect(Collectors.toList());
    }

    



}
