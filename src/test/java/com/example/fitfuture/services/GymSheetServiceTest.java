package com.example.fitfuture.services;

import com.example.fitfuture.dto.GymSheetDto;
import com.example.fitfuture.entity.Exercise;
import com.example.fitfuture.entity.GymSheet;
import com.example.fitfuture.entity.User;
import com.example.fitfuture.repository.ExerciseRepository;
import com.example.fitfuture.repository.GymSheetRepository;
import com.example.fitfuture.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GymSheetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private GymSheetRepository gymSheetRepository;

    @InjectMocks
    private GymSheetService gymSheetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addGymSheet_shouldSaveGymSheet_whenValidData() {
        // Arrange
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", null, Arrays.asList("exerciseId1", "exerciseId2"));
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA);
        List<Exercise> exercises = Arrays.asList(new Exercise("exerciseId1", "Exercise 1"), new Exercise("exerciseId2", "Exercise 2"));

        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(exerciseRepository.findAllById(gymSheetDto.getExerciseIds())).thenReturn(exercises);

        // Act
        gymSheetService.addGymSheet(gymSheetDto);

        // Assert
        ArgumentCaptor<GymSheet> gymSheetCaptor = ArgumentCaptor.forClass(GymSheet.class);
        verify(gymSheetRepository).save(gymSheetCaptor.capture());
        GymSheet savedGymSheet = gymSheetCaptor.getValue();

        assertEquals("athleteId", savedGymSheet.getAthleteId());
        assertNull(savedGymSheet.getPersonalTrainerId());
        assertEquals(2, savedGymSheet.getExerciseIds().size());
    }

    @Test
    void addGymSheet_shouldThrowException_whenAthleteNotFound() {
        // Arrange
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", null, Collections.emptyList());

        when(userRepository.findById("athleteId")).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.addGymSheet(gymSheetDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Atleta non trovato o ruolo non valido", exception.getReason());
    }

    @Test
    void addGymSheet_shouldThrowException_whenExerciseNotFound() {
        // Arrange
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", null, Arrays.asList("exerciseId1", "exerciseId2"));
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA);

        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(exerciseRepository.findAllById(gymSheetDto.getExerciseIds())).thenReturn(Collections.singletonList(new Exercise("exerciseId1", "Exercise 1")));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.addGymSheet(gymSheetDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Uno o più esercizi non trovati", exception.getReason());
    }

    @Test
    void getAllGymSheets_shouldReturnListOfGymSheets() {
        // Arrange
        GymSheet gymSheet1 = new GymSheet("gymSheetId1", "athleteId", null, Arrays.asList("exerciseId1"));
        GymSheet gymSheet2 = new GymSheet("gymSheetId2", "athleteId", null, Arrays.asList("exerciseId2"));
        when(gymSheetRepository.findAll()).thenReturn(Arrays.asList(gymSheet1, gymSheet2));

        // Act
        List<GymSheet> gymSheets = gymSheetService.getAllGymSheets();

        // Assert
        assertEquals(2, gymSheets.size());
        assertTrue(gymSheets.contains(gymSheet1));
        assertTrue(gymSheets.contains(gymSheet2));
    }

    @Test
    void updateGymSheet_shouldSaveUpdatedGymSheet() {
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA);
        User trainer = new User("ptId", "Trainer Name","trainer@mail.com", User.Role.PERSONAL_TRAINER);
        List<Exercise> exercises = Arrays.asList(new Exercise("exerciseId1", "Exercise 1"), new Exercise("exerciseId2", "Exercise 2"));


        GymSheet existingGymSheet = new GymSheet("gymSheetId", "athleteId", "ptId", Arrays.asList("exerciseId1"));
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", "ptId", Arrays.asList("exerciseId1", "exerciseId2"));

        // Simulare il comportamento del repository
        when(gymSheetRepository.findById("gymSheetId")).thenReturn(Optional.of(existingGymSheet));
        when(gymSheetRepository.save(any(GymSheet.class))).thenReturn(existingGymSheet);
        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(userRepository.findById("ptId")).thenReturn(Optional.of(trainer));
        when(exerciseRepository.findAllById(gymSheetDto.getExerciseIds())).thenReturn(exercises);

        // Act
        gymSheetService.updateGymSheet("gymSheetId", gymSheetDto);

        // Assert
        ArgumentCaptor<GymSheet> gymSheetCaptor = ArgumentCaptor.forClass(GymSheet.class);
        verify(gymSheetRepository).save(gymSheetCaptor.capture());
        GymSheet updatedGymSheet = gymSheetCaptor.getValue();

        assertEquals("gymSheetId", updatedGymSheet.getId());
        assertEquals("athleteId", updatedGymSheet.getAthleteId());
        assertEquals(2, updatedGymSheet.getExerciseIds().size());
        assertEquals(Arrays.asList("exerciseId1", "exerciseId2"), updatedGymSheet.getExerciseIds());
    }


    @Test
    void deleteGymSheet_shouldCallDeleteById() {
        // Arrange
        GymSheet existingGymSheet = new GymSheet("gS", "athleteId", "ptId", Arrays.asList("exerciseId1", "exerciseId3"));
        when(gymSheetRepository.findById("gS")).thenReturn(Optional.of(existingGymSheet));

        // Act
        gymSheetService.deleteGymSheet("gS");

        // Assert
        verify(gymSheetRepository).deleteById("gS");
    }

    @Test
    void addGymSheet_shouldSaveGymSheetWithPersonalTrainer_whenValidData() {
        // Arrange
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", "trainerId", Arrays.asList("exerciseId1", "exerciseId2"));
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA);
        User trainer = new User("trainerId", "Trainer Name","trainer@mail.com", User.Role.PERSONAL_TRAINER);
        List<Exercise> exercises = Arrays.asList(new Exercise("exerciseId1", "Exercise 1"), new Exercise("exerciseId2", "Exercise 2"));

        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(userRepository.findById("trainerId")).thenReturn(Optional.of(trainer));
        when(exerciseRepository.findAllById(gymSheetDto.getExerciseIds())).thenReturn(exercises);

        // Act
        gymSheetService.addGymSheet(gymSheetDto);

        // Assert
        ArgumentCaptor<GymSheet> gymSheetCaptor = ArgumentCaptor.forClass(GymSheet.class);
        verify(gymSheetRepository).save(gymSheetCaptor.capture());
        GymSheet savedGymSheet = gymSheetCaptor.getValue();

        assertEquals("athleteId", savedGymSheet.getAthleteId());
        assertEquals("trainerId", savedGymSheet.getPersonalTrainerId());
        assertEquals(2, savedGymSheet.getExerciseIds().size());
    }

    @Test
    void getGymSheetsByAthlete_shouldReturnGymSheetsForGivenAthlete() {
        // Arrange
        GymSheet gymSheet1 = new GymSheet("gymSheetId1", "athleteId", null, Arrays.asList("exerciseId1"));
        GymSheet gymSheet2 = new GymSheet("gymSheetId2", "athleteId", null, Arrays.asList("exerciseId2"));
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA)
                ;
        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(gymSheetRepository.findByAthleteId("athleteId")).thenReturn(Arrays.asList(gymSheet1, gymSheet2));

        // Act
        List<GymSheet> gymSheets = gymSheetService.getGymSheetsByAthlete("athleteId");

        // Assert
        assertEquals(2, gymSheets.size());
        assertTrue(gymSheets.contains(gymSheet1));
        assertTrue(gymSheets.contains(gymSheet2));
    }

    @Test
    void getGymSheetsByTrainer_shouldReturnGymSheetsForGivenTrainer() {
        // Arrange
        GymSheet gymSheet1 = new GymSheet("gymSheetId1", "trainerId", "trainerId", Arrays.asList("exerciseId1"));
        GymSheet gymSheet2 = new GymSheet("gymSheetId2", "trainerId", "trainerId", Arrays.asList("exerciseId2"));
        User trainer = new User("trainerId", "Athlete Name", "athlete@mail.com", User.Role.PERSONAL_TRAINER);

        when(userRepository.findById("trainerId")).thenReturn(Optional.of(trainer));
        when(gymSheetRepository.findByPersonalTrainerId("trainerId")).thenReturn(Arrays.asList(gymSheet1, gymSheet2));

        // Act
        List<GymSheet> gymSheets = gymSheetService.getGymSheetsByTrainer("trainerId");

        // Assert
        assertEquals(2, gymSheets.size());
        assertTrue(gymSheets.contains(gymSheet1));
        assertTrue(gymSheets.contains(gymSheet2));
    }

    @Test
    void getAllGymSheets_shouldReturnEmptyList_whenNoGymSheetsAvailable() {
        // Arrange
        when(gymSheetRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<GymSheet> gymSheets = gymSheetService.getAllGymSheets();

        // Assert
        assertTrue(gymSheets.isEmpty());
    }

}
