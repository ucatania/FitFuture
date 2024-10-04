package com.example.fitfuture.dto;


import com.example.fitfuture.entity.User;

public class UserDto {
    private String username;
    private String password;
    private String email;
    private User.Role role;

    // Costruttori, getters e setters
    public UserDto(String username, String password, String email, User.Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getters e Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}

