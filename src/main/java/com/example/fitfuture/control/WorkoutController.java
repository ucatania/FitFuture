package com.example.fitfuture.control;

import com.example.fitfuture.dto.WorkoutDto;
import com.example.fitfuture.entity.Workout;
import com.example.fitfuture.services.WorkoutService;
import com.example.fitfuture.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/athlete")
    public ResponseEntity<List<Workout>> getWorkoutsByAthlete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String athleteId = userDetails.getId();

        boolean isAthlete = userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ATLETA"));

        if (!isAthlete) {
            return ResponseEntity.status(403).body(null);
        } else {
            List<Workout> workouts = workoutService.getWorkoutsByAthlete(athleteId);
            return ResponseEntity.ok(workouts);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createWorkout(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody WorkoutDto workoutDto) {
        workoutDto.setAthleteId(userDetails.getId());
        workoutService.addWorkout(workoutDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkout(@PathVariable String id, @RequestBody WorkoutDto workoutDto) {
        workoutService.updateWorkout(id, workoutDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.ok().build();
    }
}
