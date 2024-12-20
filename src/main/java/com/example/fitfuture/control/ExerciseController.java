package com.example.fitfuture.control;

import com.example.fitfuture.dto.ExerciseDto;
import com.example.fitfuture.entity.Exercise;
import com.example.fitfuture.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public ResponseEntity<Exercise> createExercise(@RequestBody ExerciseDto exerciseDto) {
        return ResponseEntity.ok(exerciseService.createExercise(exerciseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable String id) {
        return ResponseEntity.ok(exerciseService.getExerciseById(id));
    }

    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

    // Endpoint per la ricerca per nome
    @GetMapping("/nome/{name}")
    public ResponseEntity<List<Exercise>> getExercisesByNome(@PathVariable String name) {
        return ResponseEntity.ok(exerciseService.getExercisesByNome(name));
    }

    // Endpoint per la ricerca per gruppo muscolare
    @GetMapping("/gruppo-muscolare/{gruppoMuscolare}")
    public ResponseEntity<List<Exercise>> getExercisesByGruppoMuscolare(@PathVariable String gruppoMuscolare) {
        return ResponseEntity.ok(exerciseService.getExercisesByGruppoMuscolare(gruppoMuscolare));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exercise> updateExercise(@PathVariable String id, @RequestBody ExerciseDto exerciseDto) {
        return ResponseEntity.ok(exerciseService.updateExercise(id, exerciseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable String id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/nome/{name}")
    public ResponseEntity<Void> deleteExerciseByName(@PathVariable String name) {
        exerciseService.deleteExerciseByNome(name);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/gruppo-muscolare/{gruppoMuscolare}")
    public ResponseEntity<Void> deleteExerciseByMuscleGroup(@PathVariable String gruppoMuscolare) {
        exerciseService.deleteExerciseByGruppoMuscolare(gruppoMuscolare);
        return ResponseEntity.noContent().build();
    }
}

