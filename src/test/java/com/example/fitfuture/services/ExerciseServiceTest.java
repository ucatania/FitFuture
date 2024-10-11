package com.example.fitfuture.services;

import com.example.fitfuture.dto.ExerciseDto;
import com.example.fitfuture.entity.Exercise;
import com.example.fitfuture.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExercise_shouldSaveExercise_whenValidData() {
        // Arrange
        ExerciseDto exerciseDto = new ExerciseDto("Push Up", "Chest");
        Exercise exercise = new Exercise("Push Up", "Chest");
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        // Act
        Exercise createdExercise = exerciseService.createExercise(exerciseDto);

        // Assert
        assertNotNull(createdExercise);
        assertEquals("Push Up", createdExercise.getNome());
        assertEquals("Chest", createdExercise.getGruppoMuscolare());
        verify(exerciseRepository).save(any(Exercise.class));
    }

    @Test
    void getExerciseById_shouldReturnExercise_whenIdExists() {
        // Arrange
        Exercise exercise = new Exercise("Push Up", "Chest");
        when(exerciseRepository.findById("1")).thenReturn(Optional.of(exercise));

        // Act
        Exercise foundExercise = exerciseService.getExerciseById("1");

        // Assert
        assertNotNull(foundExercise);
        assertEquals("Push Up", foundExercise.getNome());
    }

    @Test
    void getExerciseById_shouldReturnNull_whenIdDoesNotExist() {
        // Arrange
        when(exerciseRepository.findById("1")).thenReturn(Optional.empty());

        // Act
        Exercise foundExercise = exerciseService.getExerciseById("1");

        // Assert
        assertNull(foundExercise);
    }

    @Test
    void getAllExercises_shouldReturnListOfExercises() {
        // Arrange
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Squat", "Legs");
        when(exerciseRepository.findAll()).thenReturn(Arrays.asList(exercise1, exercise2));

        // Act
        List<Exercise> exercises = exerciseService.getAllExercises();

        // Assert
        assertEquals(2, exercises.size());
        assertTrue(exercises.contains(exercise1));
        assertTrue(exercises.contains(exercise2));
    }

    @Test
    void getExercisesByNome_shouldReturnMatchingExercises() {
        // Arrange
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Pull Up", "Back");
        when(exerciseRepository.findByNomeContaining("Push")).thenReturn(Arrays.asList(exercise1));

        // Act
        List<Exercise> exercises = exerciseService.getExercisesByNome("Push");

        // Assert
        assertEquals(1, exercises.size());
        assertTrue(exercises.contains(exercise1));
        assertFalse(exercises.contains(exercise2));
    }

    @Test
    void getExercisesByGruppoMuscolare_shouldReturnMatchingExercises() {
        // Arrange
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Squat", "Legs");
        when(exerciseRepository.findByGruppoMuscolare("Legs")).thenReturn(Arrays.asList(exercise2));

        // Act
        List<Exercise> exercises = exerciseService.getExercisesByGruppoMuscolare("Legs");

        // Assert
        assertEquals(1, exercises.size());
        assertTrue(exercises.contains(exercise2));
    }

    @Test
    void updateExercise_shouldUpdateExercise_whenExists() {
        // Arrange
        ExerciseDto exerciseDto = new ExerciseDto("Push Up", "Chest");
        Exercise existingExercise = new Exercise("Old Exercise", "Old Muscle Group");
        when(exerciseRepository.findById("1")).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(existingExercise);

        // Act
        Exercise updatedExercise = exerciseService.updateExercise("1", exerciseDto);

        // Assert
        assertNotNull(updatedExercise);
        assertEquals("Push Up", updatedExercise.getNome());
        assertEquals("Chest", updatedExercise.getGruppoMuscolare());
    }

    @Test
    void updateExercise_shouldThrowException_whenNotFound() {
        // Arrange
        ExerciseDto exerciseDto = new ExerciseDto("Push Up", "Chest");
        when(exerciseRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> exerciseService.updateExercise("1", exerciseDto));
        assertEquals("Exercise not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteExercise_shouldCallDeleteById_whenIdExists() {
        // Arrange
        String exerciseId = "1";

        // Act
        exerciseService.deleteExercise(exerciseId);

        // Assert
        verify(exerciseRepository).deleteById(exerciseId);
    }

    @Test
    void deleteExerciseByNome_shouldDeleteExercises_whenFound() {
        // Arrange
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Pull Up", "Back");
        when(exerciseRepository.findByNomeContaining("Push")).thenReturn(Arrays.asList(exercise1, exercise2));

        // Act
        exerciseService.deleteExerciseByNome("Push");

        // Assert
        verify(exerciseRepository).deleteAll(Arrays.asList(exercise1, exercise2));
    }

    @Test
    void deleteExerciseByNome_shouldThrowException_whenNoExercisesFound() {
        // Arrange
        when(exerciseRepository.findByNomeContaining("Not Found")).thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> exerciseService.deleteExerciseByNome("Not Found"));
        assertEquals("No exercises found with name: Not Found", exception.getMessage());
    }

    @Test
    void deleteExerciseByGruppoMuscolare_shouldDeleteExercises_whenFound() {
        // Arrange
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Bench Press", "Chest");
        when(exerciseRepository.findByGruppoMuscolare("Chest")).thenReturn(Arrays.asList(exercise1, exercise2));

        // Act
        exerciseService.deleteExerciseByGruppoMuscolare("Chest");

        // Assert
        verify(exerciseRepository).deleteAll(Arrays.asList(exercise1, exercise2));
    }

    @Test
    void deleteExerciseByGruppoMuscolare_shouldThrowException_whenNoExercisesFound() {
        // Arrange
        when(exerciseRepository.findByGruppoMuscolare("Not Found")).thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> exerciseService.deleteExerciseByGruppoMuscolare("Not Found"));
        assertEquals("No exercises found with muscle group: Not Found", exception.getMessage());
    }
}
