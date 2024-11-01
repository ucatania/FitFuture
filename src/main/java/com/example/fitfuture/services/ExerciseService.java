package com.example.fitfuture.services;

import com.example.fitfuture.dto.ExerciseDto;
import com.example.fitfuture.entity.Exercise;
import com.example.fitfuture.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise createExercise(ExerciseDto exerciseDto) {
        Exercise exercise = new Exercise(exerciseDto.getNome(), exerciseDto.getGruppoMuscolare());
        return exerciseRepository.save(exercise);
    }

    public Exercise getExerciseById(String id) {
        return exerciseRepository.findById(id).orElse(null);
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public List<Exercise> getExercisesByNome(String nome) {
        return exerciseRepository.findByNomeContaining(nome);
    }

    public List<Exercise> getExercisesByGruppoMuscolare(String gruppoMuscolare) {
        return exerciseRepository.findByGruppoMuscolare(gruppoMuscolare);
    }

    public Exercise updateExercise(String id, ExerciseDto exerciseDto) {
        Exercise existingExercise = exerciseRepository.findById(id).orElse(null);
        if (existingExercise != null) {
            existingExercise.setNome(exerciseDto.getNome());
            existingExercise.setGruppoMuscolare(exerciseDto.getGruppoMuscolare());
            return exerciseRepository.save(existingExercise);
        } else {
            throw new RuntimeException("Exercise not found with id: " + id);
        }
    }

    public void deleteExercise(String id) {
        exerciseRepository.deleteById(id);
    }

    public void deleteExerciseByNome(String nome) {
        List<Exercise> exercises = exerciseRepository.findByNomeContaining(nome);
        if (!exercises.isEmpty()) {
            exerciseRepository.deleteAll(exercises);
        } else {
            throw new RuntimeException("No exercises found with name: " + nome);
        }
    }

    public void deleteExerciseByGruppoMuscolare(String gruppoMuscolare) {
        List<Exercise> exercises = exerciseRepository.findByGruppoMuscolare(gruppoMuscolare);
        if (!exercises.isEmpty()) {
            exerciseRepository.deleteAll(exercises);
        } else {
            throw new RuntimeException("No exercises found with muscle group: " + gruppoMuscolare);
        }
    }
}
