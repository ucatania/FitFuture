package com.example.fitfuture.control;

import com.example.fitfuture.dto.WorkoutDateDto;
import com.example.fitfuture.dto.WorkoutDto;
import com.example.fitfuture.services.CalendarService;
import com.example.fitfuture.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/workouts")
    public ResponseEntity<Void> addWorkout(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody WorkoutDto workoutDto) {
        workoutDto.setAthleteId(userDetails.getId()); // Imposta l'ID dell'atleta dal contesto di sicurezza
        calendarService.addWorkoutToDate(workoutDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/workouts/{id}")
    public ResponseEntity<Void> updateWorkout(@PathVariable String id, @RequestBody WorkoutDto workoutDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        workoutDto.setAthleteId(userDetails.getId()); // Imposta l'ID dell'atleta dal contesto di sicurezza
        calendarService.updateWorkout(id, workoutDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/workouts/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        calendarService.deleteWorkout(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/athlete/workouts")
    public ResponseEntity<List<LocalDate>> getPastWorkoutDatesForAthlete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<LocalDate> pastDates = calendarService.getPastWorkoutDatesForAthlete(userDetails.getId());
        return ResponseEntity.ok(pastDates);
    }

    @GetMapping("/trainer/workouts")
    public ResponseEntity<List<WorkoutDateDto>> getPastWorkoutDatesForTrainer(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String trainerId = userDetails.getId();
        List<WorkoutDateDto> pastWorkouts = calendarService.getPastWorkoutDatesForTrainer(trainerId);
        return ResponseEntity.ok(pastWorkouts);
    }
}
