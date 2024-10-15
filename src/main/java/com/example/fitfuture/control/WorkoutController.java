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

    // Endpoint per recuperare tutti gli allenamenti
    @GetMapping
    public ResponseEntity<List<Workout>> getAllWorkouts() {
        List<Workout> workouts = workoutService.getAllWorkouts();
        return ResponseEntity.ok(workouts);
    }

    // Endpoint per recuperare gli allenamenti per l'atleta loggato
    @GetMapping("/athlete")
    public ResponseEntity<List<Workout>> getWorkoutsByAthlete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String athleteId = userDetails.getId();

        // Controllo del ruolo dell'utente
        boolean isAthlete = userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ATLETA"));

        if (!isAthlete) {
            return ResponseEntity.status(403).body(null); // Accesso negato se non è un atleta
        } else {
            List<Workout> workouts = workoutService.getWorkoutsByAthlete(athleteId); // Assicurati di avere questo metodo nel WorkoutService
            return ResponseEntity.ok(workouts);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createWorkout(
            @AuthenticationPrincipal CustomUserDetails userDetails, // Ottieni i dettagli dell'utente autenticato
            @RequestBody WorkoutDto workoutDto) {

        // Imposta l'ID dell'atleta nel workoutDto dall'utente loggato
        workoutDto.setAthleteId(userDetails.getId());

        // Chiama il servizio per aggiungere il workout
        workoutService.addWorkout(workoutDto);

        return ResponseEntity.ok().build();
    }
    // Endpoint per aggiornare un allenamento (opzionale)
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkout(@PathVariable String id, @RequestBody WorkoutDto workoutDto) {
        workoutService.updateWorkout(id, workoutDto); // Assicurati di avere questo metodo nel WorkoutService
        return ResponseEntity.ok().build();
    }

    // Endpoint per eliminare un allenamento (opzionale)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable String id) {
        workoutService.deleteWorkout(id); // Assicurati di avere questo metodo nel WorkoutService
        return ResponseEntity.ok().build();
    }
}
