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
        if (userRepository.findById(athleteId).filter(user -> user.getRole().equals(User.Role.ATLETA)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atleta non trovato.");
        }
        return gymSheetRepository.findByAthleteId(athleteId);
    }

    public List<GymSheet> getGymSheetsByTrainer(String trainerId) {
        if (userRepository.findById(trainerId).filter(user -> user.getRole().equals(User.Role.PERSONAL_TRAINER)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personal Trainer non trovato.");
        }
        return gymSheetRepository.findByPersonalTrainerId(trainerId);
    }

    public List<String> getAthletesByTrainerId(String trainerId) {
        if (userRepository.findById(trainerId).filter(user -> user.getRole().equals(User.Role.PERSONAL_TRAINER)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personal Trainer non trovato.");
        }
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
        gymSheetRepository.findById(gymSheetId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Scheda non trovata.")
        );

        if (userRepository.findById(gymSheetDto.getAthleteId()).filter(user -> user.getRole().equals(User.Role.ATLETA)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atleta non trovato o ruolo non valido.");
        }

        if (gymSheetDto.getPersonalTrainerId() != null &&
                userRepository.findById(gymSheetDto.getPersonalTrainerId()).filter(user -> user.getRole().equals(User.Role.PERSONAL_TRAINER)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personal Trainer non trovato o ruolo non valido.");
        }

        List<Exercise> exercises = exerciseRepository.findAllById(gymSheetDto.getExerciseIds());
        if (exercises.size() != gymSheetDto.getExerciseIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Uno o più esercizi non trovati.");
        }

        GymSheet gymSheet = new GymSheet(gymSheetId, gymSheetDto.getAthleteId(),
                gymSheetDto.getPersonalTrainerId(),
                gymSheetDto.getExerciseIds());
        gymSheetRepository.save(gymSheet);
    }

    public void deleteGymSheet(String gymSheetId) {
        gymSheetRepository.findById(gymSheetId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Scheda non trovata.")
        );
        gymSheetRepository.deleteById(gymSheetId);
    }


    public List<GymSheet> getGymSheetsForAuthenticatedUser(String authenticatedUserId) {
        if (userRepository.findById(authenticatedUserId).filter(user -> user.getRole().equals(User.Role.ATLETA)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utente non trovato o ruolo non valido.");
        }
        return gymSheetRepository.findAll().stream()
                .filter(gymSheet -> gymSheet.getAthleteId().equals(authenticatedUserId))
                .collect(Collectors.toList());
    }

    public GymSheet getGymSheetById(String gymSheetId) {
        return gymSheetRepository.findById(gymSheetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scheda non trovata."));
    }

    public String getPersonalTrainerByAthleteUsername(String athleteUsername) {
        // Trova l'atleta nel database
        User athlete = userRepository.findByUsername(athleteUsername);

        if (athlete == null || !athlete.getRole().equals(User.Role.ATLETA)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atleta non trovato o ruolo non valido.");
        }

        String athleteId = athlete.getId();

        // Trova le GymSheet associate all'atleta
        List<GymSheet> gymSheets = gymSheetRepository.findByAthleteId(athleteId);

        // Controlla se almeno una GymSheet ha un Personal Trainer
        for (GymSheet gymSheet : gymSheets) {
            if (gymSheet.getPersonalTrainerId() != null) {
                // Usa il metodo findById predefinito di MongoRepository che restituisce un Optional<User>
                Optional<User> personalTrainerOptional = userRepository.findById(gymSheet.getPersonalTrainerId());

                if (personalTrainerOptional.isPresent()) {
                    return personalTrainerOptional.get().getUsername();  // Restituisce il Personal Trainer
                }
            }
        }

        // Nessun PT trovato, ritorna null
        return null;  // Indica che non c'è nessun personal trainer
    }

    public List<String> getAthletesEmailsByTrainerId(String trainerId) {
        if (userRepository.findById(trainerId).filter(user -> user.getRole().equals(User.Role.PERSONAL_TRAINER)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personal Trainer non trovato.");
        }
        List<GymSheet> gymSheets = gymSheetRepository.findByPersonalTrainerId(trainerId);

        List<String> athleteIds = gymSheets.stream()
                .map(GymSheet::getAthleteId)
                .distinct()
                .collect(Collectors.toList());

        List<User> athletes = userRepository.findByIdIn(athleteIds);

        return athletes.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    public List<String> getAthleteIdsByTrainerId(String trainerId) {
        if (userRepository.findById(trainerId)
                .filter(user -> user.getRole().equals(User.Role.PERSONAL_TRAINER))
                .isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personal Trainer non trovato.");
        }

        List<GymSheet> gymSheets = gymSheetRepository.findByPersonalTrainerId(trainerId);

        return gymSheets.stream()
                .map(GymSheet::getAthleteId)
                .distinct() // Rimuove duplicati
                .collect(Collectors.toList());
    }

    public List<String> getGymSheetIdsByAthlete(String athleteId) {
        if (userRepository.findById(athleteId).filter(user -> user.getRole().equals(User.Role.ATLETA)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atleta non trovato.");
        }

        return gymSheetRepository.findByAthleteId(athleteId)
                .stream()
                .map(GymSheet::getId)
                .collect(Collectors.toList());
    }



}
