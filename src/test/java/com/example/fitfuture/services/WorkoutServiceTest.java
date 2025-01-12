package com.example.fitfuture.services;

import com.example.fitfuture.dto.WorkoutDto;
import com.example.fitfuture.entity.GymSheet;
import com.example.fitfuture.entity.Workout;
import com.example.fitfuture.exceptions.GymSheetNotFoundException;
import com.example.fitfuture.exceptions.UserNotFoundException;
import com.example.fitfuture.exceptions.WorkoutNotFoundException;
import com.example.fitfuture.repository.GymSheetRepository;
import com.example.fitfuture.repository.UserRepository;
import com.example.fitfuture.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    private WorkoutRepository workoutRepository;
    private UserRepository userRepository;
    private GymSheetRepository gymSheetRepository;
    private WorkoutService workoutService;

    @BeforeEach
    void setUp() {
        workoutRepository = Mockito.mock(WorkoutRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        gymSheetRepository = Mockito.mock(GymSheetRepository.class);
        workoutService = new WorkoutService(workoutRepository, userRepository, gymSheetRepository);
    }

    @Test
    void testAddWorkout_Success_WithAuthenticatedUser() {
        // Dati del WorkoutDto e dell'utente autenticato
        WorkoutDto workoutDto = new WorkoutDto("athlete1", "gymSheet1", LocalDate.now(), "Notes");
        GymSheet gymSheet = new GymSheet("gymSheet1", "athlete1", "trainer1", Arrays.asList("exercise1"));

        // Simulazione di un utente autenticato (Spring Security)
        UserDetails authenticatedUser = org.springframework.security.core.userdetails.User
                .withUsername("athlete1")
                .password("password")
                .roles("USER")
                .build();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authenticatedUser, null));

        // Configurazione dei mock
        when(userRepository.existsById(workoutDto.getAthleteId())).thenReturn(true);
        when(gymSheetRepository.findById(workoutDto.getGymSheetId())).thenReturn(Optional.of(gymSheet));

        // Invoca il servizio
        workoutService.addWorkout(workoutDto);

        // Verifica che il metodo `save` sia stato invocato
        verify(workoutRepository, times(1)).save(any(Workout.class));
    }


    @Test
    void testAddWorkout_AthleteNotFound() {
        WorkoutDto workoutDto = new WorkoutDto("athlete1", "gymSheet1", LocalDate.now(), "Notes");
        when(userRepository.existsById(workoutDto.getAthleteId())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> workoutService.addWorkout(workoutDto));
    }

    @Test
    void testAddWorkout_GymSheetNotFound() {
        WorkoutDto workoutDto = new WorkoutDto("athlete1", "gymSheet1", LocalDate.now(), "Notes");
        when(userRepository.existsById(workoutDto.getAthleteId())).thenReturn(true);
        when(gymSheetRepository.findById(workoutDto.getGymSheetId())).thenReturn(Optional.empty());

        assertThrows(GymSheetNotFoundException.class, () -> workoutService.addWorkout(workoutDto));
    }

    @Test
    void testUpdateWorkout_Success() {
        String workoutId = "workout1";
        WorkoutDto workoutDto = new WorkoutDto("athlete1", "gymSheet1", LocalDate.now(), "Updated notes");
        Workout existingWorkout = new Workout("athlete1", "gymSheet1", LocalDate.now(), "Notes");

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(existingWorkout));
        when(gymSheetRepository.existsById(workoutDto.getGymSheetId())).thenReturn(true);

        workoutService.updateWorkout(workoutId, workoutDto);

        verify(workoutRepository, times(1)).save(existingWorkout);
    }

    @Test
    void testUpdateWorkout_WorkoutNotFound() {
        String workoutId = "workout1";
        WorkoutDto workoutDto = new WorkoutDto("athlete1", "gymSheet1", LocalDate.now(), "Updated notes");

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> workoutService.updateWorkout(workoutId, workoutDto));
    }

    @Test
    void testDeleteWorkout_Success() {
        String workoutId = "workout1";
        Workout existingWorkout = new Workout("athlete1", "gymSheet1", LocalDate.now(), "Notes");

        when(workoutRepository.findById(workoutId)).thenReturn(Optional.of(existingWorkout));

        workoutService.deleteWorkout(workoutId);

        verify(workoutRepository, times(1)).delete(existingWorkout);
    }

    @Test
    void testDeleteWorkout_WorkoutNotFound() {
        String workoutId = "workout1";
        when(workoutRepository.findById(workoutId)).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFoundException.class, () -> workoutService.deleteWorkout(workoutId));
    }
}
