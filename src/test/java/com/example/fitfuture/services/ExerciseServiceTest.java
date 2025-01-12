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
        ExerciseDto exerciseDto = new ExerciseDto("Push Up", "Chest");
        Exercise exercise = new Exercise("Push Up", "Chest");
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        Exercise createdExercise = exerciseService.createExercise(exerciseDto);

        assertNotNull(createdExercise);
        assertEquals("Push Up", createdExercise.getNome());
        assertEquals("Chest", createdExercise.getGruppoMuscolare());
        verify(exerciseRepository).save(any(Exercise.class));
    }

    @Test
    void getExerciseById_shouldReturnExercise_whenIdExists() {
        Exercise exercise = new Exercise("Push Up", "Chest");
        when(exerciseRepository.findById("1")).thenReturn(Optional.of(exercise));

        Exercise foundExercise = exerciseService.getExerciseById("1");

        assertNotNull(foundExercise);
        assertEquals("Push Up", foundExercise.getNome());
    }

    @Test
    void getExerciseById_shouldReturnNull_whenIdDoesNotExist() {
        when(exerciseRepository.findById("1")).thenReturn(Optional.empty());

        Exercise foundExercise = exerciseService.getExerciseById("1");

        assertNull(foundExercise);
    }

    @Test
    void getAllExercises_shouldReturnListOfExercises() {
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Squat", "Legs");
        when(exerciseRepository.findAll()).thenReturn(Arrays.asList(exercise1, exercise2));

        List<Exercise> exercises = exerciseService.getAllExercises();

        assertEquals(2, exercises.size());
        assertTrue(exercises.contains(exercise1));
        assertTrue(exercises.contains(exercise2));
    }

    @Test
    void getExercisesByNome_shouldReturnMatchingExercises() {
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Pull Up", "Back");
        when(exerciseRepository.findByNomeContaining("Push")).thenReturn(Arrays.asList(exercise1));

        List<Exercise> exercises = exerciseService.getExercisesByNome("Push");

        assertEquals(1, exercises.size());
        assertTrue(exercises.contains(exercise1));
        assertFalse(exercises.contains(exercise2));
    }

    @Test
    void getExercisesByGruppoMuscolare_shouldReturnMatchingExercises() {
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Squat", "Legs");
        when(exerciseRepository.findByGruppoMuscolare("Legs")).thenReturn(Arrays.asList(exercise2));

        List<Exercise> exercises = exerciseService.getExercisesByGruppoMuscolare("Legs");

        assertEquals(1, exercises.size());
        assertTrue(exercises.contains(exercise2));
    }

    @Test
    void updateExercise_shouldUpdateExercise_whenExists() {
        ExerciseDto exerciseDto = new ExerciseDto("Push Up", "Chest");
        Exercise existingExercise = new Exercise("Old Exercise", "Old Muscle Group");
        when(exerciseRepository.findById("1")).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(existingExercise);

        Exercise updatedExercise = exerciseService.updateExercise("1", exerciseDto);

        assertNotNull(updatedExercise);
        assertEquals("Push Up", updatedExercise.getNome());
        assertEquals("Chest", updatedExercise.getGruppoMuscolare());
    }

    @Test
    void updateExercise_shouldThrowException_whenNotFound() {
        ExerciseDto exerciseDto = new ExerciseDto("Push Up", "Chest");
        when(exerciseRepository.findById("1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> exerciseService.updateExercise("1", exerciseDto));
        assertEquals("Exercise not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteExercise_shouldCallDeleteById_whenIdExists() {
        String exerciseId = "1";

        exerciseService.deleteExercise(exerciseId);

        verify(exerciseRepository).deleteById(exerciseId);
    }

    @Test
    void deleteExerciseByNome_shouldDeleteExercises_whenFound() {
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Pull Up", "Back");
        when(exerciseRepository.findByNomeContaining("Push")).thenReturn(Arrays.asList(exercise1, exercise2));

        exerciseService.deleteExerciseByNome("Push");

        verify(exerciseRepository).deleteAll(Arrays.asList(exercise1, exercise2));
    }

    @Test
    void deleteExerciseByNome_shouldThrowException_whenNoExercisesFound() {
        when(exerciseRepository.findByNomeContaining("Not Found")).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> exerciseService.deleteExerciseByNome("Not Found"));
        assertEquals("No exercises found with name: Not Found", exception.getMessage());
    }

    @Test
    void deleteExerciseByGruppoMuscolare_shouldDeleteExercises_whenFound() {
        Exercise exercise1 = new Exercise("Push Up", "Chest");
        Exercise exercise2 = new Exercise("Bench Press", "Chest");
        when(exerciseRepository.findByGruppoMuscolare("Chest")).thenReturn(Arrays.asList(exercise1, exercise2));

        exerciseService.deleteExerciseByGruppoMuscolare("Chest");

        verify(exerciseRepository).deleteAll(Arrays.asList(exercise1, exercise2));
    }

    @Test
    void deleteExerciseByGruppoMuscolare_shouldThrowException_whenNoExercisesFound() {
        when(exerciseRepository.findByGruppoMuscolare("Not Found")).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> exerciseService.deleteExerciseByGruppoMuscolare("Not Found"));
        assertEquals("No exercises found with muscle group: Not Found", exception.getMessage());
    }
}
