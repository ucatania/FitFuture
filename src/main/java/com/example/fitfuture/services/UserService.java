package com.example.fitfuture.services;

import com.example.fitfuture.entity.User;
import com.example.fitfuture.exceptions.UserNotFoundException;
import com.example.fitfuture.exceptions.UsernameAlreadyExistsException;
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
            throw new UsernameAlreadyExistsException("Username"+ user.getUsername() +" already exists");
        }

        user.setPassword(SecurityConfig.encodePassword(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUserEmail(String username, User user) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            // Aggiorna solo l'email, lasciando invariati gli altri attributi
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                existingUser.setEmail(user.getEmail());
            }
            return userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException(username);
        }
    }

    public User updateUserPassword(String username, User user) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            // Aggiorna solo la password se fornita
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(SecurityConfig.encodePassword(user.getPassword()));
            }
            // Salva l'utente con la nuova password
            return userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException(username);
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
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }
}
