package com.example.fitfuture.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Endpoint to retrieve gym sheets for a specific athlete
    @GetMapping("/athlete/{athleteId}")
    public ResponseEntity<List<GymSheet>> getGymSheetsByAthlete(@PathVariable String athleteId) {
        List<GymSheet> gymSheets = gymSheetService.getGymSheetsByAthlete(athleteId);
        return ResponseEntity.ok(gymSheets);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<GymSheet>> getGymSheetsByTrainer(@PathVariable String trainerId) {
        List<GymSheet> gymSheets = gymSheetService.getGymSheetsByTrainer(trainerId);
        return ResponseEntity.ok(gymSheets);
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
