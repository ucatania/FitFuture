package com.example.fitfuture.services;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Costruttore per l'iniezione delle dipendenze
    public GymSheetService(UserRepository userRepository, ExerciseRepository exerciseRepository, GymSheetRepository gymSheetRepository) {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.gymSheetRepository = gymSheetRepository;
    }
    // Method to add a new gym sheet
    public void addGymSheet(GymSheetDto gymSheetDto) {

        Optional<User> athleteOpt = userRepository.findById(gymSheetDto.getAthleteId());
        if (athleteOpt.isEmpty() || !athleteOpt.get().getRole().equals(User.Role.ATLETA)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Atleta non trovato o ruolo non valido");
        }

        List<Exercise> exercises = exerciseRepository.findAllById(gymSheetDto.getExerciseIds());
        if (exercises.size() != gymSheetDto.getExerciseIds().size()) {
            // Se uno o più esercizi non vengono trovati, lancia un'eccezione HTTP 500
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

    // Getter di tutte le GymSheet
    public List<GymSheet> getAllGymSheets() {
        return gymSheetRepository.findAll();
    }

    // Getter di tutte le GymSheet di un atleta
    public List<GymSheet> getGymSheetsByAthlete(String athleteId) {
        return gymSheetRepository.findByAthleteId(athleteId);
    }

    public List<GymSheet> getGymSheetsByTrainer(String trainerId) {
        return gymSheetRepository.findByPersonalTrainerId(trainerId);
    }

    public List<String> getAthletesByTrainerId(String trainerId) {
        // Getter di tutte le GymSheet associate al personal trainer
        List<GymSheet> gymSheets = gymSheetRepository.findByPersonalTrainerId(trainerId);

        // Estrae gli athleteId dalle GymSheet
        List<String> athleteIds = gymSheets.stream()
                .map(GymSheet::getAthleteId)
                .distinct()
                .collect(Collectors.toList());

        // Trova gli utenti corrispondenti agli athleteId
        List<User> athletes = userRepository.findByIdIn(athleteIds);

        // Estrae e ritorna gli username degli atleti
        return athletes.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        }

        // Metodo per aggiornare una GymSheet
    public void updateGymSheet(String gymSheetId, GymSheetDto gymSheetDto) {
        GymSheet gymSheet = new GymSheet(gymSheetId, gymSheetDto.getAthleteId(),
                gymSheetDto.getPersonalTrainerId(),
                gymSheetDto.getExerciseIds());
        gymSheetRepository.save(gymSheet);
    }

    // Metodo per cancellare una GymSheet
    public void deleteGymSheet(String gymSheetId) {
        gymSheetRepository.deleteById(gymSheetId);
    }

    public List<GymSheet> getGymSheetsForAuthenticatedUser(String authenticatedUserId) {
        return gymSheetRepository.findAll().stream()
                .filter(gymSheet -> gymSheet.getAthleteId().equals(authenticatedUserId))
                .collect(Collectors.toList());
    }


}
