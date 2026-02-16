package com.sharko.yura.taskflow.dto;

import com.sharko.yura.taskflow.entity.Role;

import java.time.LocalDateTime;

public class UserResponseDTO {

    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    public UserResponseDTO(String username, String email, Role role, LocalDateTime createdAt) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public UserResponseDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }

}
