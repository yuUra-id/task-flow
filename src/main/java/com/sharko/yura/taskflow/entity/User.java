package com.sharko.yura.taskflow.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Сущность пользователя.
 * Представляет зарегистрированного пользователя,
 * который может создавать задачи и быть назначенным исполнителем задач.
 * Пользователь содержит данные для аутентификации
 * и авторизации в системе (username, email, password, role).
 * Связи
 * One-to-Many с Task (createdTasks) — задачи, созданные пользователем
 * One-to-Many с Task (executorTasks) — задачи, назначенные пользователю
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true, length = 50)
    private String username;
    @Column(nullable = false,unique = true, length = 50)
    private String email;
    @Column(nullable = false, length = 255)
    private String password;
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "creator")
    private List<Task> createdTasks = new ArrayList<>();
    @OneToMany(mappedBy = "executor")
    private List<Task> executorTasks = new ArrayList<>();

    public User() {}

    public User(Role role, String password, String email, String username) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Task> getCreatedTasks() {
        return createdTasks;
    }

    public void addCreatedTask(Task task) {

        createdTasks.add(task);
        task.setCreator(this);

    }

    public void addExecutorTask(Task task) {

        executorTasks.add(task);
        task.setExecutor(this);

    }

    public void removeCreatedTask(Task task) {
        createdTasks.remove(task);
    }

    public void removeExecutorTask(Task task) {
        executorTasks.remove(task);
        task.setExecutor(null);
    }

    public List<Task> getExecutorTasks() {
        return executorTasks;
    }

    public void setExecutorTasks(List<Task> executorTasks) {
        this.executorTasks = executorTasks;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }

}
