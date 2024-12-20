package com.example.fitfuture.security;

import com.example.fitfuture.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private String id; // ID dell'utente
    private String username; // Nome utente
    private String password; // Password dell'utente
    private Collection<? extends GrantedAuthority> authorities; // Ruoli/Permessi dell'utente


    // Costruttore
    public CustomUserDetails(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }



    public String getId() {
        return id;
    }

    // Restituisce il nome utente
    @Override
    public String getUsername() {
        return username;
    }

    // Restituisce la password
    @Override
    public String getPassword() {
        return password;
    }

    // Restituisce le autorità (ruoli) associate a questo utente
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Restituisce true se l'account non è scaduto
    @Override
    public boolean isAccountNonExpired() {
        return true; // Implementa la logica se necessario
    }

    // Restituisce true se l'account non è bloccato
    @Override
    public boolean isAccountNonLocked() {
        return true; // Implementa la logica se necessario
    }

    // Restituisce true se le credenziali non sono scadute
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implementa la logica se necessario
    }

    // Restituisce true se l'account è abilitato
    @Override
    public boolean isEnabled() {
        return true; // Implementa la logica se necessario
    }
}
