package com.example.fitfuture.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.fitfuture.security.CustomUserDetails;

import java.util.List;
import com.example.fitfuture.services.GymSheetService;
import com.example.fitfuture.entity.*;
import com.example.fitfuture.dto.GymSheetDto;

@RestController
@RequestMapping("/api/gymSheets")
public class GymSheetController {

    private final GymSheetService gymSheetService;

    @Autowired
    public GymSheetController(GymSheetService gymSheetService) {
        this.gymSheetService = gymSheetService;
    }

    // Endpoint per ottenere l'ID dell'atleta da una scheda
    @GetMapping("/athlete/id")
    public ResponseEntity<String> getAthleteIdFromGymSheet(
            @RequestParam String gymSheetId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        GymSheet gymSheet = gymSheetService.getGymSheetById(gymSheetId);

        boolean isAthlete = userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ATLETA"));

        if (!isAthlete) {
            return ResponseEntity.status(403).body("Accesso negato. Solo un atleta può accedere a questo endpoint.");
        }

        return ResponseEntity.ok(gymSheet.getAthleteId());
    }

    // Endpoint per ottenere l'ID del personal trainer da una scheda
    @GetMapping("/trainer/id")
    public ResponseEntity<String> getTrainerIdFromGymSheet(
            @RequestParam String gymSheetId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        GymSheet gymSheet = gymSheetService.getGymSheetById(gymSheetId);

        boolean isTrainer = userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("PERSONAL_TRAINER"));

        if (!isTrainer) {
            return ResponseEntity.status(403).body("Accesso negato. Solo un personal trainer può accedere a questo endpoint.");
        }

        return ResponseEntity.ok(gymSheet.getPersonalTrainerId());
    }

    // Endpoint per ottenere tutte le schede
    @GetMapping
    public ResponseEntity<List<GymSheet>> getAllGymSheets() {
        List<GymSheet> gymSheets = gymSheetService.getAllGymSheets();
        return ResponseEntity.ok(gymSheets);
    }

    // Endpoint per ottenere le schede di un atleta autenticato
    @GetMapping("/athlete")
    public ResponseEntity<List<GymSheet>> getGymSheetsByAthlete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String athleteId = userDetails.getId();

        boolean isAthlete = userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ATLETA"));

        if (!isAthlete) {
            return ResponseEntity.status(403).body(null);
        }

        List<GymSheet> gymSheets = gymSheetService.getGymSheetsByAthlete(athleteId);
        return ResponseEntity.ok(gymSheets);
    }

    // Endpoint per ottenere le schede di un personal trainer autenticato
    @GetMapping("/trainer")
    public ResponseEntity<List<GymSheet>> getGymSheetsByTrainer(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String trainerId = userDetails.getId();

        boolean isTrainer = userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("PERSONAL_TRAINER"));

        if (!isTrainer) {
            return ResponseEntity.status(403).body(null);
        }

        List<GymSheet> gymSheets = gymSheetService.getGymSheetsByTrainer(trainerId);
        return ResponseEntity.ok(gymSheets);
    }

    // Endpoint per ottenere la lista degli atleti associati a un personal trainer
    @GetMapping("/trainer/list-of-athlete")
    public ResponseEntity<List<String>> getAthleteByTrainer(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String trainerId = userDetails.getId();

        boolean isPT = userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("PERSONAL_TRAINER"));

        if (!isPT) {
            return ResponseEntity.status(403).body(null); // Accesso negato se non è un personal trainer
        }

        List<String> athletes = gymSheetService.getAthletesByTrainerId(trainerId);
        return ResponseEntity.ok(athletes);
    }



    // Endpoint per creare una nuova scheda
    @PostMapping("/createGymSheet")
    public ResponseEntity<Void> createGymSheet(@RequestBody GymSheetDto gymSheetDto) {
        gymSheetService.addGymSheet(gymSheetDto);
        return ResponseEntity.ok().build();
    }

    // Endpoint per aggiornare una scheda
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGymSheet(@PathVariable String id, @RequestBody GymSheetDto gymSheetDto) {
        gymSheetService.updateGymSheet(id, gymSheetDto);
        return ResponseEntity.ok().build();
    }

    // Endpoint per eliminare una scheda
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGymSheet(@PathVariable String id) {
        gymSheetService.deleteGymSheet(id);
        return ResponseEntity.ok().build();
    }
}

