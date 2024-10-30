package com.example.fitfuture.services;

import com.example.fitfuture.entity.GymSheet;
import com.example.fitfuture.entity.User;
import com.example.fitfuture.entity.Workout;
import com.example.fitfuture.dto.WorkoutDto;
import com.example.fitfuture.repository.GymSheetRepository;
import com.example.fitfuture.repository.UserRepository;
import com.example.fitfuture.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final GymSheetRepository gymSheetRepository;

    @Autowired

    // Costruttore
    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository, GymSheetRepository gymSheetRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.gymSheetRepository = gymSheetRepository;
    }

    // Metodo per aggiungere un allenamento
    public void addWorkout(WorkoutDto workoutDto) {
        // Controlla se l'atleta esiste
        Optional<User> athlete = userRepository.findById(workoutDto.getAthleteId());
        if (athlete.isEmpty()) {
            throw new RuntimeException("Athlete not found.");
        }

        // Controlla se la GymSheet esiste
        Optional<GymSheet> gymSheet = gymSheetRepository.findById(workoutDto.getGymSheetId());
        if (gymSheet.isEmpty()) {
            throw new RuntimeException("GymSheet not found.");
        }

        // Verifica se la GymSheet è associata all'atleta loggato
        if (!gymSheet.get().getAthleteId().equals(workoutDto.getAthleteId())) {
            throw new RuntimeException("GymSheet is not associated with the logged-in athlete.");
        }

        // Crea il nuovo Workout
        Workout workout = new Workout(workoutDto.getAthleteId(), workoutDto.getGymSheetId(), workoutDto.getDate());
        workoutRepository.save(workout);
    }


    // Getters
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    public List<Workout> getWorkoutsByAthlete(String athleteId) {
        return workoutRepository.findByAthleteId(athleteId);
    }

    // Metodo per aggiornare un allenamento
    public void updateWorkout(String id, WorkoutDto workoutDto) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isEmpty()) {
            throw new RuntimeException("Workout not found.");
        }

        Workout workout = optionalWorkout.get();
        workout.setDate(workoutDto.getDate());
        workout.setGymSheetId(workoutDto.getGymSheetId());
        workoutRepository.save(workout);
    }

    // Metodo per cancellare un allenamento
    public void deleteWorkout(String id) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isEmpty()) {
            throw new RuntimeException("Workout not found.");
        }

        workoutRepository.delete(optionalWorkout.get()); // Elimina il workout
    }
}
