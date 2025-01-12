package com.example.fitfuture.services;

import com.example.fitfuture.dto.WorkoutDto;
import com.example.fitfuture.entity.Workout;
import com.example.fitfuture.exceptions.WorkoutNotFoundException;
import com.example.fitfuture.repository.GymSheetRepository;
import com.example.fitfuture.repository.UserRepository;
import com.example.fitfuture.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalendarServiceTest {

    private WorkoutRepository workoutRepository;
    private GymSheetRepository gymSheetRepository;
    private UserRepository userRepository;
    private CalendarService calendarService;

    @BeforeEach
    void setUp() {
        workoutRepository = Mockito.mock(WorkoutRepository.class);
        gymSheetRepository = Mockito.mock(GymSheetRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        calendarService = new CalendarService(workoutRepository, gymSheetRepository, userRepository);
    }

    @Test
    void testAddWorkoutToDate_Success() {
        WorkoutDto workoutDto = new WorkoutDto("athlete1", "gymSheet1", LocalDate.now(), "Test notes");
        when(userRepository.existsById(workoutDto.getAthleteId())).thenReturn(true);
        when(gymSheetRepository.existsById(workoutDto.getGymSheetId())).thenReturn(true);

        calendarService.addWorkoutToDate(workoutDto);

        verify(workoutRepository, times(1)).save(any(Workout.class));
    }

    @Test
    void testAddWorkoutToDate_AthleteNotFound() {
        WorkoutDto workoutDto = new WorkoutDto("athlete1", "gymSheet1", LocalDate.now(), "Test notes");
        when(userRepository.existsById(workoutDto.getAthleteId())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> calendarService.addWorkoutToDate(workoutDto));
    }

    @Test
    void testUpdateWorkout_Success() {
        String workoutId = "workout1";
        WorkoutDto workoutDto = new WorkoutDto("athlete1", "gymSheet1", LocalDate.now(), "Updated notes");

        Workout existingWorkout = new Workout("athlete1", "gymSheet1", LocalDate.now(), "Test notes");
        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(existingWorkout));
        when(gymSheetRepository.existsById(workoutDto.getGymSheetId())).thenReturn(true);

        calendarService.updateWorkout(workoutId, workoutDto);

        verify(workoutRepository, times(1)).save(existingWorkout);
    }

    @Test
    void testDeleteWorkout_Success() {
        String workoutId = "workout1";
        Workout existingWorkout = new Workout("athlete1", "gymSheet1", LocalDate.now(), "Test notes");

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(existingWorkout));

        calendarService.deleteWorkout(workoutId, "athlete1");

        verify(workoutRepository, times(1)).delete(existingWorkout);
    }

    @Test
    void testDeleteWorkout_WorkoutNotFound() {
        String workoutId = "workout1";
        when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> calendarService.deleteWorkout(workoutId, "athlete1"));
    }
}
