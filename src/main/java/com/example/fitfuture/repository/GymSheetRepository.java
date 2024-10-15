package com.example.fitfuture.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.fitfuture.entity.GymSheet;

import java.util.List;

@Repository
public interface GymSheetRepository extends MongoRepository<GymSheet, String> {
    List<GymSheet> findByAthleteId(String athleteId);
    List<GymSheet> findByPersonalTrainerId(String trainerId);
}
