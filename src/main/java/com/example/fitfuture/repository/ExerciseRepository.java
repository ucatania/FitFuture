package com.example.fitfuture.repository;

import com.example.fitfuture.entity.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {

    Optional<Exercise> findByNome(String nome); // Cerca un esercizio con nome esatto

    List<Exercise> findByNomeContaining(String nome);

    List<Exercise> findByGruppoMuscolare(String gruppoMuscolare);
}
