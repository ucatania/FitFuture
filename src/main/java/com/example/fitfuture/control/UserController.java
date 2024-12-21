package com.example.fitfuture.control;

import com.example.fitfuture.dto.LoginRequest;
import com.example.fitfuture.dto.UserDto;
import com.example.fitfuture.entity.User;
import com.example.fitfuture.services.UserService;
import com.example.fitfuture.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityConfig securityConfig;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, SecurityConfig securityConfig) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.securityConfig = securityConfig;
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
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Autenticazione dell'utente
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Genera il token JWT usando la classe SecurityConfig
            String token = securityConfig.generateToken(authentication);

            // Risposta JSON con il token
            Map<String, String> response = new HashMap<>();
            response.put("authToken", token);
            response.put("username", authentication.getName());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail(), userDto.getRole());
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists: " + userDto.getUsername());
        }
    }

    @GetMapping("/{username}/getUsername")
    public ResponseEntity<String> getUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username).getUsername());
    }

    @GetMapping("/{username}/getUserID")
    public ResponseEntity<String> getUserID(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username).getId());
    }

    @GetMapping("/{username}/getEmail")
    public ResponseEntity<String> getEmail(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username).getEmail());
    }

    @GetMapping("/{username}/getRole")
    public ResponseEntity<String> getRole(@PathVariable String username) {
        String role = String.valueOf(userService.getUser(username).getRole());
        if ("ATLETA".equals(role)) {
            return ResponseEntity.ok("ATLETA");
        } else if ("PERSONAL_TRAINER".equals(role)) {
            return ResponseEntity.ok("PERSONAL TRAINER");
        } else {
            return ResponseEntity.ok(role);
        }
    }

    @PutMapping("/changeEmail?username={value}")
    public ResponseEntity<User> changeEmail(@PathVariable String username, @RequestParam String newEmail) {
        User user = userService.getUser(username);
        user.setEmail(newEmail);
        return ResponseEntity.ok(userService.updateUser(username, user));
    }

    @PutMapping("/{username}/changeUsername")
    public ResponseEntity<User> changeUsername(@PathVariable String username, @RequestParam String newUsername) {
        User user = userService.getUser(username);
        user.setUsername(newUsername);
        return ResponseEntity.ok(userService.updateUser(username, user));
    }

    @PutMapping("/{username}/changePassword")
    public ResponseEntity<User> changePassword(@PathVariable String username, @RequestParam String newPassword) {
        User user = userService.getUser(username);
        user.setPassword(newPassword);
        return ResponseEntity.ok(userService.updateUser(username, user));
    }
}



