package com.example.fitfuture.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.fitfuture.entity.Workout;

public interface WorkoutRepository extends MongoRepository<Workout, String> {
    List<Workout> findByAthleteId(String athleteId); // Trova allenamenti per atleta
    List<Workout> findByAthleteIdAndDateLessThanEqual(String athleteId, LocalDate date);
    List<Workout> findByAthleteIdInAndDateLessThanEqual(List<String> athleteIds, LocalDate date);

}


