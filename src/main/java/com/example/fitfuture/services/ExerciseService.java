package com.example.fitfuture.services;

import com.example.fitfuture.dto.ExerciseDto;
import com.example.fitfuture.entity.Exercise;
import com.example.fitfuture.exceptions.ExerciseAlreadyExistingException;
import com.example.fitfuture.exceptions.ExerciseNotFoundException;
import com.example.fitfuture.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    // Creazione di un esercizio con controllo di unicità del nome
    public Exercise createExercise(ExerciseDto exerciseDto) {
        // Controlla se esiste già un esercizio con lo stesso nome
        Optional<Exercise> existingExercise = exerciseRepository.findByNome(exerciseDto.getNome());
        if (existingExercise.isPresent()) {
            throw new ExerciseAlreadyExistingException("This exercise already exists");
        }

        // Se non esiste, lo crea e lo salva
        Exercise exercise = new Exercise(exerciseDto.getNome(), exerciseDto.getGruppoMuscolare());
        return exerciseRepository.save(exercise);
    }

    // Ottieni un esercizio per ID
    public Exercise getExerciseById(String id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Esercizio non trovato con ID: " + id));
    }

    // Ottieni tutti gli esercizi
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    // Ottieni tutti gli esercizi con un determinato nome (ricerca parziale)
    public List<Exercise> getExercisesByNome(String nome) {
        return exerciseRepository.findByNomeContaining(nome);
    }

    // Ottieni tutti gli esercizi per gruppo muscolare
    public List<Exercise> getExercisesByGruppoMuscolare(String gruppoMuscolare) {
        return exerciseRepository.findByGruppoMuscolare(gruppoMuscolare);
    }

    // Aggiorna un esercizio esistente
    public Exercise updateExercise(String id, ExerciseDto exerciseDto) {
        Exercise existingExercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with id: " + id));

        // Controlla se esiste un altro esercizio con lo stesso nome (ma ID diverso)
        Optional<Exercise> exerciseWithSameName = exerciseRepository.findByNome(exerciseDto.getNome());
        if (exerciseWithSameName.isPresent() && !exerciseWithSameName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Un altro esercizio con questo nome esiste già!");
        }

        // Aggiorna i campi
        existingExercise.setNome(exerciseDto.getNome());
        existingExercise.setGruppoMuscolare(exerciseDto.getGruppoMuscolare());

        return exerciseRepository.save(existingExercise);
    }

    // Elimina un esercizio per ID
    public void deleteExercise(String id) {
        if (!exerciseRepository.existsById(id)) {
            throw new IllegalArgumentException("Esercizio non trovato con ID: " + id);
        }
        exerciseRepository.deleteById(id);
    }

    // Elimina un esercizio per nome
    public void deleteExerciseByNome(String nome) {
        List<Exercise> exercises = exerciseRepository.findByNomeContaining(nome);
        if (exercises.isEmpty()) {
            throw new IllegalArgumentException("No exercises found with name: " + nome);
        }
        exerciseRepository.deleteAll(exercises);
    }

    public void deleteExerciseByGruppoMuscolare(String gruppoMuscolare) {
        List<Exercise> exercises = exerciseRepository.findByGruppoMuscolare(gruppoMuscolare);
        if (!exercises.isEmpty()) {
            exerciseRepository.deleteAll(exercises);
        } else {
            throw new ExerciseNotFoundException("No exercises found with muscle group: " + gruppoMuscolare);
        }
    }

        // Ottieni gli ID degli esercizi in base ai nomi
    public List<String> getExerciseIdsByNames(List<String> exerciseNames) {
        return exerciseRepository.findAll().stream()
                .filter(exercise -> exerciseNames.contains(exercise.getNome()))
                .map(Exercise::getId)
                .toList();
    }
}
