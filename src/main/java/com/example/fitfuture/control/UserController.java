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

@CrossOrigin(origins = "*") // Permette richieste da qualsiasi dominio (puoi limitarlo se vuoi)
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

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Generazione del token, ma non viene restituito
            securityConfig.generateBase64Token(loginRequest.getUsername(), loginRequest.getPassword());

            // Restituzione di una risposta vuota
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(null); // Se fallisce, ritorna un errore con status 401
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

    @PutMapping("/changeEmail")
    public ResponseEntity<User> changeEmail(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String newEmail = request.get("newEmail");

        if (username == null || newEmail == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.getUser(username);
        user.setEmail(newEmail);
        return ResponseEntity.ok(userService.updateUserEmail(username, user));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<User> changePassword(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String newPassword = requestBody.get("newPassword");

        if (username == null || newPassword == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.getUser(username);
        user.setPassword(newPassword);
        return ResponseEntity.ok(userService.updateUserPassword(username, user));
    }


    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestParam String username) {
        User user = userService.getUser(username);
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUsername")
    public ResponseEntity<String> getUsernameById(@RequestParam String userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user.getUsername());
    }

    @GetMapping("/getUserID")
    public ResponseEntity<String> getUserID(@RequestParam String username) {
        User user = userService.getUser(username);
        return ResponseEntity.ok(user.getId());
    }

    @GetMapping("/getEmail")
    public ResponseEntity<String> getEmail(@RequestParam String username) {
        User user = userService.getUser(username);
        return ResponseEntity.ok(user.getEmail());
    }

    @GetMapping("/getRole")
    public ResponseEntity<User.Role> getRole(@RequestParam String username) {
        User user = userService.getUser(username);
        return ResponseEntity.ok(user.getRole());
    }

    @GetMapping("/getRoleAsInt")
    public ResponseEntity<Integer> getRoleAsInt(@RequestParam String username) {
        return ResponseEntity.ok(userService.getRoleAsInt(username));
    }
}