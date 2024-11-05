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
        // Arrange
        User user = new User("john_doe", "password", "john@example.com", User.Role.ATLETA);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertNotNull(createdUser);
        assertEquals("john_doe", createdUser.getUsername());
        assertEquals("john@example.com", createdUser.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void getUser_shouldReturnUser_whenUsernameExists() {
        // Arrange
        User user = new User("john_doe", "password", "john@example.com", User.Role.ATLETA);
        when(userRepository.findByUsername("john_doe")).thenReturn(user);

        // Act
        User foundUser = userService.getUser("john_doe");

        // Assert
        assertNotNull(foundUser);
        assertEquals("john_doe", foundUser.getUsername());
    }

    @Test
    void getUser_shouldReturnNull_whenUsernameDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("john_doe")).thenReturn(null);

        // Act
        User foundUser = userService.getUser("john_doe");

        // Assert
        assertNull(foundUser);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        // Arrange
        User user1 = new User("john_doe", "password", "john@example.com", User.Role.ATLETA);
        User user2 = new User("jane_doe", "password", "jane@example.com", User.Role.PERSONAL_TRAINER);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void updateUser_shouldThrowException_whenNotFound() {
        // Arrange
        User updateUser = new User("john_doe", "new_password", "new_email@example.com", User.Role.ATLETA);
        when(userRepository.findByUsername("john_doe")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser("john_doe", updateUser));
        assertEquals("User not found with username: john_doe", exception.getMessage());
    }

    @Test
    void deleteUser_shouldCallDelete_whenUserExists() {
        // Arrange
        User user = new User("john_doe", "password", "john@example.com", User.Role.ATLETA);
        when(userRepository.findByUsername("john_doe")).thenReturn(user);

        // Act
        userService.deleteUser("john_doe");

        // Assert
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldNotCallDelete_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("john_doe")).thenReturn(null);

        // Act
        userService.deleteUser("john_doe");

        // Assert
        verify(userRepository, never()).delete(any(User.class));
    }
}
