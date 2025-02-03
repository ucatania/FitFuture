package com.example.fitfuture.control;

import com.example.fitfuture.dto.ExerciseDto;
import com.example.fitfuture.entity.Exercise;
import com.example.fitfuture.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/createExercise")
    public ResponseEntity<Exercise> createExercise(@RequestBody ExerciseDto exerciseDto) {
        return ResponseEntity.ok(exerciseService.createExercise(exerciseDto));
    }

    @GetMapping("/id")
    public ResponseEntity<Exercise> getExerciseById(@RequestParam String id) {
        return ResponseEntity.ok(exerciseService.getExerciseById(id));
    }

    @GetMapping("/getAllExercises")
    public ResponseEntity<List<Exercise>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

    // Endpoint per la ricerca per nome
    @GetMapping("/nome")
    public ResponseEntity<List<Exercise>> getExercisesByNome(@RequestParam String name) {
        return ResponseEntity.ok(exerciseService.getExercisesByNome(name));
    }

    // Endpoint per la ricerca per gruppo muscolare
    @GetMapping("/gruppo-muscolare")
    public ResponseEntity<List<Exercise>> getExercisesByGruppoMuscolare(@RequestParam String gruppoMuscolare) {
        return ResponseEntity.ok(exerciseService.getExercisesByGruppoMuscolare(gruppoMuscolare));
    }

    @PutMapping("/updateExercise")
    public ResponseEntity<Exercise> updateExercise(@RequestParam String id, @RequestBody ExerciseDto exerciseDto) {
        return ResponseEntity.ok(exerciseService.updateExercise(id, exerciseDto));
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<Void> deleteExercise(@RequestParam String id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteByName")
    public ResponseEntity<Void> deleteExerciseByName(@RequestParam String name) {
        exerciseService.deleteExerciseByNome(name);
        return ResponseEntity.noContent().build();
    }

}

