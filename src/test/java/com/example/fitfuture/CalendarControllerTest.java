package com.example.fitfuture;

import com.example.fitfuture.control.CalendarController;
import com.example.fitfuture.dto.WorkoutDateDto;
import com.example.fitfuture.dto.WorkoutDto;
import com.example.fitfuture.security.CustomUserDetails;
import com.example.fitfuture.services.CalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class CalendarControllerTest {

    @Mock
    private CalendarService calendarService;

    @InjectMocks
    private CalendarController calendarController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(calendarController).build();
    }

    @Test
    public void testAddWorkout() throws Exception {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setGymSheetId("gymSheetId");
        workoutDto.setDate(LocalDate.now());
        workoutDto.setNotes("Notes");

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn("athleteId");

        mockMvc.perform(post("/api/calendar/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gymSheetId\":\"gymSheetId\",\"date\":\"2024-10-29\",\"notes\":\"Notes\"}")
                        .principal(() -> userDetails.getId()))
                .andExpect(status().isOk());

        ArgumentCaptor<WorkoutDto> workoutDtoCaptor = ArgumentCaptor.forClass(WorkoutDto.class);
        verify(calendarService, times(1)).addWorkoutToDate(workoutDtoCaptor.capture());
        assert workoutDtoCaptor.getValue().getAthleteId().equals("athleteId");
    }

    @Test
    public void testGetPastWorkoutDatesForAthlete() throws Exception {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn("athleteId");
        when(calendarService.getPastWorkoutDatesForAthlete("athleteId")).thenReturn(Arrays.asList(LocalDate.now()));

        mockMvc.perform(get("/api/calendar/athlete/workouts")
                        .principal(() -> userDetails.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(LocalDate.now().toString()));
    }

    @Test
    public void testGetPastWorkoutDatesForTrainer() throws Exception {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn("trainerId");

        WorkoutDateDto workoutDateDto = new WorkoutDateDto(LocalDate.now(), "athleteUser");
        when(calendarService.getPastWorkoutDatesForTrainer("trainerId")).thenReturn(Arrays.asList(workoutDateDto));

        mockMvc.perform(get("/api/calendar/trainer/workouts")
                        .principal(() -> userDetails.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[0].username").value("athleteUser"));
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/calendar/athlete/workouts"))
                .andExpect(status().isUnauthorized());
    }
}
