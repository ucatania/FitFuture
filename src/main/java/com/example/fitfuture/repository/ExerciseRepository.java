package com.example.fitfuture.repository;

import com.example.fitfuture.entity.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {
    List<Exercise> findByNomeContaining(String nome);
    List<Exercise> findByGruppoMuscolare(String gruppoMuscolare);
}

