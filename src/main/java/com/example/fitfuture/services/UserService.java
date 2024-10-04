package com.example.fitfuture.services;

import com.example.fitfuture.entity.User;
import com.example.fitfuture.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
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
            // Aggiorna solo i campi desiderati
            existingUser.setPassword(user.getPassword());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            return userRepository.save(existingUser);
        } else {
            // Gestisci il caso in cui l'utente non esiste
            throw new RuntimeException("User not found with username: " + username);
        }
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
