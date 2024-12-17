package com.example.fitfuture.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.fitfuture.services.UserService;
import org.springframework.security.core.Authentication;


import javax.crypto.SecretKey;
import java.util.Date;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);// Chiave segreta per la firma del token JWT

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/api/users/register").permitAll()
                .requestMatchers("/api/users/login").permitAll()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/workouts").authenticated()
                .anyRequest().permitAll()
                .and().httpBasic();
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * Metodo per generare un token JWT.
     * @param authentication Oggetto contenente le informazioni dell'utente autenticato.
     * @return Token JWT firmato.
     */
    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName()) // Nome utente autenticato
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Scadenza: 1 ora
                .signWith(SignatureAlgorithm.HS256, secretKey) // Firma del token
                .compact();
    }
}
