package com.example.fitfuture.services;

import com.example.fitfuture.entity.User;
import com.example.fitfuture.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldSaveUser_whenValidData() {
        User user = new User("john_doe", "password", "john@example.com", User.Role.ATLETA);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("john_doe", createdUser.getUsername());
        assertEquals("john@example.com", createdUser.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void getUser_shouldReturnUser_whenUsernameExists() {
        User user = new User("john_doe", "password", "john@example.com", User.Role.ATLETA);
        when(userRepository.findByUsername("john_doe")).thenReturn(user);

        User foundUser = userService.getUser("john_doe");

        assertNotNull(foundUser);
        assertEquals("john_doe", foundUser.getUsername());
    }

    @Test
    void getUser_shouldReturnNull_whenUsernameDoesNotExist() {
        when(userRepository.findByUsername("john_doe")).thenReturn(null);

        User foundUser = userService.getUser("john_doe");

        assertNull(foundUser);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user1 = new User("john_doe", "password", "john@example.com", User.Role.ATLETA);
        User user2 = new User("jane_doe", "password", "jane@example.com", User.Role.PERSONAL_TRAINER);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void updateUserPassword_shouldThrowException_whenNotFound() {
        User updateUser = new User("john_doe", "new_password", "new_email@example.com", User.Role.ATLETA);
        when(userRepository.findByUsername("john_doe")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUserPassword("john_doe", updateUser));

        assertEquals("User not found with username: john_doe", exception.getMessage());
    }

    @Test
    void updateUserEmail_shouldThrowException_whenNotFound() {
        User updateUser = new User("john_doe", "new_password", "new_email@example.com", User.Role.ATLETA);
        when(userRepository.findByUsername("john_doe")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUserEmail("john_doe", updateUser));

        assertEquals("User not found with username: john_doe", exception.getMessage());
    }


    @Test
    void deleteUser_shouldCallDelete_whenUserExists() {
        User user = new User("john_doe", "password", "john@example.com", User.Role.ATLETA);
        when(userRepository.findByUsername("john_doe")).thenReturn(user);

        userService.deleteUser("john_doe");

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldNotCallDelete_whenUserDoesNotExist() {
        when(userRepository.findByUsername("john_doe")).thenReturn(null);

        userService.deleteUser("john_doe");

        verify(userRepository, never()).delete(any(User.class));
    }
    @Test
    void getRoleAsInt_shouldReturnCorrectRoleValue() {
        User personalTrainer = new User("trainer_user", "password", "trainer@example.com", User.Role.PERSONAL_TRAINER);
        User athlete = new User("athlete_user", "password", "athlete@example.com", User.Role.ATLETA);

        when(userRepository.findByUsername("trainer_user")).thenReturn(personalTrainer);
        when(userRepository.findByUsername("athlete_user")).thenReturn(athlete);
        when(userRepository.findByUsername("non_existent")).thenReturn(null);

        assertEquals(1, userService.getRoleAsInt("trainer_user"), "Expected 1 for PERSONAL_TRAINER role");
        assertEquals(0, userService.getRoleAsInt("athlete_user"), "Expected 0 for ATLETA role");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getRoleAsInt("non_existent"));

        // Modifica il test per allinearlo alla tua eccezione attuale
        assertEquals("User not found with username: non_existent", exception.getMessage());
    }

}
