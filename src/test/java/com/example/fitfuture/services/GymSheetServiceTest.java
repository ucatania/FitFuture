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
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", null, Arrays.asList("exerciseId1", "exerciseId2"));
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA);
        List<Exercise> exercises = Arrays.asList(new Exercise("exerciseId1", "Exercise 1"), new Exercise("exerciseId2", "Exercise 2"));

        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(exerciseRepository.findAllById(gymSheetDto.getExerciseIds())).thenReturn(exercises);

        gymSheetService.addGymSheet(gymSheetDto);

        ArgumentCaptor<GymSheet> gymSheetCaptor = ArgumentCaptor.forClass(GymSheet.class);
        verify(gymSheetRepository).save(gymSheetCaptor.capture());
        GymSheet savedGymSheet = gymSheetCaptor.getValue();

        assertEquals("athleteId", savedGymSheet.getAthleteId());
        assertNull(savedGymSheet.getPersonalTrainerId());
        assertEquals(2, savedGymSheet.getExerciseIds().size());
    }

    @Test
    void addGymSheet_shouldThrowException_whenAthleteNotFound() {
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", null, Arrays.asList("exerciseId1"));

        when(userRepository.findById("athleteId")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.addGymSheet(gymSheetDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Atleta non trovato o ruolo non valido", exception.getReason());
    }

    @Test
    void addGymSheet_shouldThrowException_whenExerciseListIsNull() {
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", null, null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.addGymSheet(gymSheetDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("La lista degli esercizi non può essere nulla o vuota", exception.getReason());
    }

    @Test
    void addGymSheet_shouldThrowException_whenExerciseListIsEmpty() {
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", null, Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.addGymSheet(gymSheetDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("La lista degli esercizi non può essere nulla o vuota", exception.getReason());
    }

    @Test
    void addGymSheet_shouldThrowException_whenExerciseNotFound() {
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", null, Arrays.asList("exerciseId1", "exerciseId2"));
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA);

        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(exerciseRepository.findAllById(gymSheetDto.getExerciseIds())).thenReturn(Collections.singletonList(new Exercise("exerciseId1", "Exercise 1")));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.addGymSheet(gymSheetDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Uno o più esercizi non trovati", exception.getReason());
    }

    @Test
    void getAllGymSheets_shouldReturnListOfGymSheets() {
        GymSheet gymSheet1 = new GymSheet("gymSheetId1", "athleteId", null, Arrays.asList("exerciseId1"));
        GymSheet gymSheet2 = new GymSheet("gymSheetId2", "athleteId", null, Arrays.asList("exerciseId2"));
        when(gymSheetRepository.findAll()).thenReturn(Arrays.asList(gymSheet1, gymSheet2));

        List<GymSheet> gymSheets = gymSheetService.getAllGymSheets();

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

        when(gymSheetRepository.findById("gymSheetId")).thenReturn(Optional.of(existingGymSheet));
        when(gymSheetRepository.save(any(GymSheet.class))).thenReturn(existingGymSheet);
        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(userRepository.findById("ptId")).thenReturn(Optional.of(trainer));
        when(exerciseRepository.findAllById(gymSheetDto.getExerciseIds())).thenReturn(exercises);

        gymSheetService.updateGymSheet("gymSheetId", gymSheetDto);

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
        GymSheet existingGymSheet = new GymSheet("gS", "athleteId", "ptId", Arrays.asList("exerciseId1", "exerciseId3"));
        when(gymSheetRepository.findById("gS")).thenReturn(Optional.of(existingGymSheet));

        // Act
        gymSheetService.deleteGymSheet("gS");

        // Assert
        verify(gymSheetRepository).deleteById("gS");
    }

    @Test
    void addGymSheet_shouldSaveGymSheetWithPersonalTrainer_whenValidData() {
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", "trainerId", Arrays.asList("exerciseId1", "exerciseId2"));
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA);
        User trainer = new User("trainerId", "Trainer Name","trainer@mail.com", User.Role.PERSONAL_TRAINER);
        List<Exercise> exercises = Arrays.asList(new Exercise("exerciseId1", "Exercise 1"), new Exercise("exerciseId2", "Exercise 2"));

        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(userRepository.findById("trainerId")).thenReturn(Optional.of(trainer));
        when(exerciseRepository.findAllById(gymSheetDto.getExerciseIds())).thenReturn(exercises);

                gymSheetService.addGymSheet(gymSheetDto);

        ArgumentCaptor<GymSheet> gymSheetCaptor = ArgumentCaptor.forClass(GymSheet.class);
        verify(gymSheetRepository).save(gymSheetCaptor.capture());
        GymSheet savedGymSheet = gymSheetCaptor.getValue();

        assertEquals("athleteId", savedGymSheet.getAthleteId());
        assertEquals("trainerId", savedGymSheet.getPersonalTrainerId());
        assertEquals(2, savedGymSheet.getExerciseIds().size());
    }

    @Test
    void getGymSheetsByAthlete_shouldReturnGymSheetsForGivenAthlete() {
        GymSheet gymSheet1 = new GymSheet("gymSheetId1", "athleteId", null, Arrays.asList("exerciseId1"));
        GymSheet gymSheet2 = new GymSheet("gymSheetId2", "athleteId", null, Arrays.asList("exerciseId2"));
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA);

        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(gymSheetRepository.findByAthleteId("athleteId")).thenReturn(Arrays.asList(gymSheet1, gymSheet2));

        List<GymSheet> gymSheets = gymSheetService.getGymSheetsByAthlete("athleteId");

        assertEquals(2, gymSheets.size());
        assertTrue(gymSheets.contains(gymSheet1));
        assertTrue(gymSheets.contains(gymSheet2));
    }

    @Test
    void getGymSheetsByTrainer_shouldReturnGymSheetsForGivenTrainer() {
        GymSheet gymSheet1 = new GymSheet("gymSheetId1", "trainerId", "trainerId", Arrays.asList("exerciseId1"));
        GymSheet gymSheet2 = new GymSheet("gymSheetId2", "trainerId", "trainerId", Arrays.asList("exerciseId2"));
        User trainer = new User("trainerId", "Athlete Name", "athlete@mail.com", User.Role.PERSONAL_TRAINER);

        when(userRepository.findById("trainerId")).thenReturn(Optional.of(trainer));
        when(gymSheetRepository.findByPersonalTrainerId("trainerId")).thenReturn(Arrays.asList(gymSheet1, gymSheet2));

        List<GymSheet> gymSheets = gymSheetService.getGymSheetsByTrainer("trainerId");

        assertEquals(2, gymSheets.size());
        assertTrue(gymSheets.contains(gymSheet1));
        assertTrue(gymSheets.contains(gymSheet2));
    }

    @Test
    void getAllGymSheets_shouldReturnEmptyList_whenNoGymSheetsAvailable() {
        when(gymSheetRepository.findAll()).thenReturn(Collections.emptyList());

        List<GymSheet> gymSheets = gymSheetService.getAllGymSheets();

        assertTrue(gymSheets.isEmpty());
    }

    @Test
    void getGymSheetsByAthlete_shouldReturnGymSheets_whenAthleteExists() {
        User athlete = new User("athleteId", "Athlete Name", "athlete@mail.com", User.Role.ATLETA);
        GymSheet gymSheet1 = new GymSheet("gymSheetId1", "athleteId", null, Arrays.asList("exerciseId1"));
        GymSheet gymSheet2 = new GymSheet("gymSheetId2", "athleteId", null, Arrays.asList("exerciseId2"));

        when(userRepository.findById("athleteId")).thenReturn(Optional.of(athlete));
        when(gymSheetRepository.findByAthleteId("athleteId")).thenReturn(Arrays.asList(gymSheet1, gymSheet2));

        List<GymSheet> gymSheets = gymSheetService.getGymSheetsByAthlete("athleteId");

        assertEquals(2, gymSheets.size());
        assertTrue(gymSheets.contains(gymSheet1));
        assertTrue(gymSheets.contains(gymSheet2));
    }

    @Test
    void getGymSheetsByAthlete_shouldThrowException_whenAthleteNotFound() {
        when(userRepository.findById("athleteId")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.getGymSheetsByAthlete("athleteId"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Atleta non trovato.", exception.getReason());
    }

    @Test
    void deleteGymSheet_shouldThrowException_whenGymSheetNotFound() {
        when(gymSheetRepository.findById("gymSheetId")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.deleteGymSheet("gymSheetId"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Scheda non trovata.", exception.getReason());
    }

    @Test
    void testGetAthletesByTrainerId_Success() {
        // Setup
        String trainerId = "trainer123";
        String athleteId1 = "athlete1";
        String athleteId2 = "athlete2";
        List<GymSheet> gymSheets = Arrays.asList(
                new GymSheet("1", athleteId1, trainerId, Collections.singletonList("exercise1")),
                new GymSheet("2", athleteId2, trainerId, Collections.singletonList("exercise2"))
        );

        User athlete1 = new User(athleteId1, "athlete1", "athlete1@example.com", User.Role.ATLETA);
        User athlete2 = new User(athleteId2, "athlete2", "athlete2@example.com", User.Role.ATLETA);

        when(userRepository.findById(trainerId)).thenReturn(Optional.of(new User(trainerId, "trainer", "trainer@example.com", User.Role.PERSONAL_TRAINER)));
        when(gymSheetRepository.findByPersonalTrainerId(trainerId)).thenReturn(gymSheets);
        when(userRepository.findByIdIn(Arrays.asList(athleteId1, athleteId2))).thenReturn(Arrays.asList(athlete1, athlete2));

        // Execute
        List<String> result = gymSheetService.getAthletesByTrainerId(trainerId);

        // Verify
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("athlete1"));
        assertTrue(result.contains("athlete2"));
    }

    @Test
    void testGetAthletesByTrainerId_TrainerNotFound() {
        // Setup
        String trainerId = "trainer123";

        when(userRepository.findById(trainerId)).thenReturn(Optional.empty());

        // Execute and Verify
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            gymSheetService.getAthletesByTrainerId(trainerId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Personal Trainer non trovato.", exception.getReason());
    }

    @Test
    void getGymSheetsByTrainer_shouldThrowException_whenTrainerNotFound() {
        when(userRepository.findById("trainerId")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.getGymSheetsByTrainer("trainerId"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Personal Trainer non trovato.", exception.getReason());
    }
    
    @Test
    void getGymSheetById_shouldThrowException_whenGymSheetNotFound() {
        when(gymSheetRepository.findById("gymSheetId")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.getGymSheetById("gymSheetId"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Scheda non trovata.", exception.getReason());
    }

    @Test
    void testGetAthletesByTrainerId_NoAthletes() {
        // Setup
        String trainerId = "trainer123";

        when(userRepository.findById(trainerId)).thenReturn(Optional.of(new User(trainerId, "trainer", "trainer@example.com", User.Role.PERSONAL_TRAINER)));
        when(gymSheetRepository.findByPersonalTrainerId(trainerId)).thenReturn(Collections.emptyList());

        // Execute
        List<String> result = gymSheetService.getAthletesByTrainerId(trainerId);

        // Verify
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getGymSheetById_shouldReturnGymSheet_whenFound() {
        GymSheet gymSheet = new GymSheet("gymSheetId", "athleteId", "trainerId", Arrays.asList("exerciseId1"));
        when(gymSheetRepository.findById("gymSheetId")).thenReturn(Optional.of(gymSheet));

        GymSheet result = gymSheetService.getGymSheetById("gymSheetId");

        assertNotNull(result);
        assertEquals("gymSheetId", result.getId());
    }

    @Test
    void getGymSheetById_shouldThrowException_whenNotFound() {
        when(gymSheetRepository.findById("gymSheetId")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.getGymSheetById("gymSheetId"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }


    @Test
    void updateGymSheet_shouldThrowException_whenGymSheetNotFound() {
        GymSheetDto gymSheetDto = new GymSheetDto("athleteId", "trainerId", Arrays.asList("exerciseId1"));
        when(gymSheetRepository.findById("gymSheetId")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gymSheetService.updateGymSheet("gymSheetId", gymSheetDto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
