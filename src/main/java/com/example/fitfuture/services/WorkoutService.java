package com.example.fitfuture.services;

import com.example.fitfuture.entity.GymSheet;
import com.example.fitfuture.entity.User;
import com.example.fitfuture.entity.Workout;
import com.example.fitfuture.dto.WorkoutDto;
import com.example.fitfuture.exceptions.GymSheetNotFoundException;
import com.example.fitfuture.exceptions.UserNotFoundException;
import com.example.fitfuture.exceptions.WorkoutNotFoundException;
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
    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository, GymSheetRepository gymSheetRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.gymSheetRepository = gymSheetRepository;
    }

    public void addWorkout(WorkoutDto workoutDto) {
        Optional<User> athlete = userRepository.findById(workoutDto.getAthleteId());
        if (athlete.isEmpty()) {
            throw new UserNotFoundException("Athlete not found.");
        }

        Optional<GymSheet> gymSheet = gymSheetRepository.findById(workoutDto.getGymSheetId());
        if (gymSheet.isEmpty()) {
            throw new GymSheetNotFoundException("GymSheet not found.");
        }

        if (!gymSheet.get().getAthleteId().equals(workoutDto.getAthleteId())) {
            throw new GymSheetNotFoundException("GymSheet is not associated with the logged-in athlete.");
        }

        Workout workout = new Workout(
                workoutDto.getAthleteId(),
                workoutDto.getGymSheetId(),
                workoutDto.getDate(),
                workoutDto.getNotes()
        );

        workoutRepository.save(workout);
    }

    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    public List<Workout> getWorkoutsByAthlete(String athleteId) {
        return workoutRepository.findByAthleteId(athleteId);
    }

    public void updateWorkout(String id, WorkoutDto workoutDto) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isEmpty()) {
            throw new WorkoutNotFoundException("Workout not found.");
        }

        Workout workout = optionalWorkout.get();
        workout.setDate(workoutDto.getDate());
        workout.setGymSheetId(workoutDto.getGymSheetId());

        if (workoutDto.getNotes() != null) {
            workout.setNotes(workoutDto.getNotes());
        }

        workoutRepository.save(workout);
    }

    public void deleteWorkout(String id) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isEmpty()) {
            throw new WorkoutNotFoundException("Workout not found.");
        }
        workoutRepository.delete(optionalWorkout.get());
    }
}
