package com.example.fitfuture.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.fitfuture.entity.Workout;

public interface WorkoutRepository extends MongoRepository<Workout, String> {
    List<Workout> findByAthleteId(String athleteId); // Trova allenamenti per atleta
}

