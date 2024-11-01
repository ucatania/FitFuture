package com.example.fitfuture.services;

import com.example.fitfuture.dto.WorkoutDateDto;
import com.example.fitfuture.dto.WorkoutDto;
import com.example.fitfuture.entity.GymSheet;
import com.example.fitfuture.entity.User;
import com.example.fitfuture.entity.Workout;
import com.example.fitfuture.repository.GymSheetRepository;
import com.example.fitfuture.repository.UserRepository;
import com.example.fitfuture.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private final WorkoutRepository workoutRepository;
    private final GymSheetRepository gymSheetRepository;
    private final UserRepository userRepository;

    @Autowired
    public CalendarService(WorkoutRepository workoutRepository, GymSheetRepository gymSheetRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.gymSheetRepository = gymSheetRepository;
        this.userRepository = userRepository;
    }

    public void addWorkoutToDate(WorkoutDto workoutDto) {
        Workout workout = new Workout(workoutDto.getAthleteId(), workoutDto.getGymSheetId(), workoutDto.getDate(), workoutDto.getNotes());
        workoutRepository.save(workout);
    }

    public void updateWorkout(String id, WorkoutDto workoutDto) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isPresent() && optionalWorkout.get().getAthleteId().equals(workoutDto.getAthleteId())) {
            Workout workout = optionalWorkout.get();
            workout.setDate(workoutDto.getDate());
            workout.setGymSheetId(workoutDto.getGymSheetId());
            workout.setNotes(workoutDto.getNotes());
            workoutRepository.save(workout);
        } else {
            throw new RuntimeException("Workout not found or athlete does not match.");
        }
    }

    public void deleteWorkout(String id, String athleteId) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isPresent() && optionalWorkout.get().getAthleteId().equals(athleteId)) {
            workoutRepository.delete(optionalWorkout.get());
        } else {
            throw new RuntimeException("Workout not found or athlete does not match.");
        }
    }

    public List<LocalDate> getPastWorkoutDatesForAthlete(String athleteId) {
        return workoutRepository.findByAthleteIdAndDateLessThanEqual(athleteId, LocalDate.now())
                .stream()
                .map(Workout::getDate)
                .collect(Collectors.toList());
    }

    public List<WorkoutDateDto> getPastWorkoutDatesForTrainer(String personalTrainerId) {
        List<GymSheet> gymSheets = gymSheetRepository.findByPersonalTrainerId(personalTrainerId);
        if (gymSheets.isEmpty()) {
            return Collections.emptyList(); // Restituisci lista vuota se non ci sono atleti
        }

        List<String> athleteIds = gymSheets.stream()
                .map(GymSheet::getAthleteId)
                .collect(Collectors.toList());

        List<Workout> workouts = workoutRepository.findByAthleteIdInAndDateLessThanEqual(athleteIds, LocalDate.now());

        return workouts.stream()
                .map(workout -> {
                    Optional<User> athlete = userRepository.findById(workout.getAthleteId());
                    String username = athlete.map(User::getUsername).orElse("Unknown User");
                    return new WorkoutDateDto(workout.getDate(), username);
                })
                .collect(Collectors.toList());
    }
}
