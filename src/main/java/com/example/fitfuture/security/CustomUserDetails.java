package com.example.fitfuture.security;

import com.example.fitfuture.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private String id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // Costruttore
    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        // Mappiamo il ruolo dell'utente in un oggetto GrantedAuthority
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    //Getters
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    // Restituisce true se l'account non è scaduto
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Restituisce true se l'account non è bloccato
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Restituisce true se le credenziali non sono scadute
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Restituisce true se l'account è abilitato
    @Override
    public boolean isEnabled() {
        return true;
    }
}
