package com.example.fitfuture.security;

import com.example.fitfuture.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private String id; 
    private String username; 
    private String password; 
    private Collection<? extends GrantedAuthority> authorities; 


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

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implementa la logica se necessario
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implementa la logica se necessario
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implementa la logica se necessario
    }

    @Override
    public boolean isEnabled() {
        return true; // Implementa la logica se necessario
    }
}
