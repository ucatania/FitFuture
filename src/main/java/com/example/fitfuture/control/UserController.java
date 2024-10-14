package com.example.fitfuture.control;

import com.example.fitfuture.dto.LoginRequest;
import com.example.fitfuture.dto.UserDto;
import com.example.fitfuture.entity.User;
import com.example.fitfuture.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getRole());
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody UserDto userDto) {
        User user = new User(username, userDto.getPassword(), userDto.getEmail(), userDto.getRole());
        return ResponseEntity.ok(userService.updateUser(username, user));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            return ResponseEntity.ok("Login successful! User: " + authentication.getName());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getRole());
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            // Restituisce un errore 409 se lo username esiste già
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists: " + userDto.getUsername());
        }
    }

}


