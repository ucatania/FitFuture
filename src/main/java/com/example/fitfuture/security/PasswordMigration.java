package com.example.fitfuture.security;
import com.example.fitfuture.entity.User;
import com.example.fitfuture.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class PasswordMigration {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Costruttore
    @Autowired
    public PasswordMigration(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Questo metodo verrà eseguito una sola volta al lancio dell'applicazione
    @PostConstruct
    public void migratePasswords() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            String password = user.getPassword();

            // Verifica se la password è già codificata
            if (!password.startsWith("$2a$")) { // Le password codificate iniziano con "$2a$"
                // Codifica la password con BCrypt
                String encodedPassword = passwordEncoder.encode(password);
                user.setPassword(encodedPassword);
                userRepository.save(user); // Aggiorna l'utente con la password codificata
            }
        }
    }
}
