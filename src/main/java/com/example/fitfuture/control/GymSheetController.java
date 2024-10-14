package com.example.fitfuture.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.fitfuture.security.CustomUserDetails;

import java.util.Collection;
import java.util.List;
import com.example.fitfuture.services.GymSheetService;
import com.example.fitfuture.entity.GymSheet;
import com.example.fitfuture.dto.GymSheetDto;

@RestController
@RequestMapping("/api/gymSheets")
public class GymSheetController {

    private final GymSheetService gymSheetService;

    @Autowired
    public GymSheetController(GymSheetService gymSheetService) {
        this.gymSheetService = gymSheetService;
    }

    // Endpoint to retrieve all gym sheets
    @GetMapping
    public ResponseEntity<List<GymSheet>> getAllGymSheets() {
        List<GymSheet> gymSheets = gymSheetService.getAllGymSheets();
        return ResponseEntity.ok(gymSheets);
    }

    // Endpoint to retrieve gym sheets for the logged-in athlete
    @GetMapping("/athlete")
    public ResponseEntity<List<GymSheet>> getGymSheetsByAthlete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // Assumiamo che l'ID dell'utente sia memorizzato nel nome utente
        String athleteId = userDetails.getUsername();

        // Controllo del ruolo dell'utente
        boolean isAthlete = userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ATLETA"));

        if (!isAthlete) {
            return ResponseEntity.status(403).body(null); // Forbid access if not athlete
        } else{
            // Recupera le schede di allenamento dell'atleta loggato
            List<GymSheet> gymSheets = gymSheetService.getGymSheetsByAthlete(athleteId);
            return ResponseEntity.ok(gymSheets);
        }
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<GymSheet>> getGymSheetsByTrainer(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String trainerId = userDetails.getUsername();

        // Controllo del ruolo dell'utente
        boolean isPT = userDetails.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("PERSONAL_TRAINER"));

        if (!isPT) {
            return ResponseEntity.status(403).body(null); // Forbid access if not athlete
        } else {
            List<GymSheet> gymSheets = gymSheetService.getGymSheetsByTrainer(trainerId);
            return ResponseEntity.ok(gymSheets);
        }
    }

    // Endpoint to create a new gym sheet
    @PostMapping
    public ResponseEntity<Void> createGymSheet(@RequestBody GymSheetDto gymSheetDto) {
        gymSheetService.addGymSheet(gymSheetDto);
        return ResponseEntity.ok().build();
    }

    // Endpoint to update a gym sheet
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGymSheet(@PathVariable String id, @RequestBody GymSheetDto gymSheetDto) {
        gymSheetService.updateGymSheet(id, gymSheetDto);
        return ResponseEntity.ok().build();
    }

    // Endpoint to delete a gym sheet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGymSheet(@PathVariable String id) {
        gymSheetService.deleteGymSheet(id);
        return ResponseEntity.ok().build();
    }
}
