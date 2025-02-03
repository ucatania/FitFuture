package com.example.fitfuture.services;

import com.example.fitfuture.dto.ExerciseDto;
import com.example.fitfuture.entity.Exercise;
import com.example.fitfuture.exceptions.ExerciseNotFoundException;
import com.example.fitfuture.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new ExerciseNotFoundException("Exercise not found with id: " + id);
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
            throw new ExerciseNotFoundException("No exercises found with name: " + nome);
        }
    }

    public void deleteExerciseByGruppoMuscolare(String gruppoMuscolare) {
        List<Exercise> exercises = exerciseRepository.findByGruppoMuscolare(gruppoMuscolare);
        if (!exercises.isEmpty()) {
            exerciseRepository.deleteAll(exercises);
        } else {
            throw new ExerciseNotFoundException("No exercises found with muscle group: " + gruppoMuscolare);
        }
    }

    public List<String> getExerciseIdsByNames(List<String> exerciseNames) {
        // Recupera tutti gli esercizi dal repository
        List<Exercise> exercises = exerciseRepository.findAll();

        // Filtra gli esercizi per nome e ottieni la lista degli ID
        return exercises.stream()
                .filter(exercise -> exerciseNames.contains(exercise.getNome())) // Filtra per nome
                .map(Exercise::getId) // Estrai gli ID
                .collect(Collectors.toList()); // Restituisci la lista di ID
    }




}
