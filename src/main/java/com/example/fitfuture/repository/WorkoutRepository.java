package com.example.fitfuture.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.fitfuture.entity.Workout;

public interface WorkoutRepository extends MongoRepository<Workout, String> {
    List<Workout> findByAthleteId(String athleteId);
    List<Workout> findByAthleteIdAndDateLessThanEqual(String athleteId, LocalDate date);
    List<Workout> findByAthleteIdInAndDateLessThanEqual(List<String> athleteIds, LocalDate date);

}


