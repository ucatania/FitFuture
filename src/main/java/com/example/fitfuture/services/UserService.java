package com.example.fitfuture.services;

import com.example.fitfuture.entity.User;
import com.example.fitfuture.repository.UserRepository;
import com.example.fitfuture.security.CustomUserDetails; // Importa la CustomUserDetails
import com.example.fitfuture.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {

        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }

        user.setPassword(SecurityConfig.encodePassword(user.getPassword())); // Codifica la password
        return userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(String username, User user) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(SecurityConfig.encodePassword(user.getPassword())); // Codifica la password
            }
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // Crea e restituisci un'istanza di CustomUserDetails
        return new CustomUserDetails(user);
    }
}
