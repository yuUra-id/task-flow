package com.sharko.yura.taskflow.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


/**
 * Сущность задачи.
 * Представляет задачу, которая создаётся пользователем
 * и может быть назначена другому пользователю для выполнения.
 * Основные характеристики задачи:
 * title — название задачи
 * description — описание
 * status — текущий статус выполнения (NEW, IN_PROGRESS, DONE)
 * priority — приоритет задачи (LOW, MEDIUM, HIGH)
 * deadline — срок выполнения
 * Связи
 * Many-to-One с User (creator) — пользователь, создавший задачу
 * Many-to-One с User (executor) — пользователь, назначенный исполнителем
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String title;
    @Column(nullable = false, length = 255)
    private String description;
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    @ManyToOne()
    @JoinColumn(name = "id_creator")
    private User creator;
    @ManyToOne()
    @JoinColumn(name = "id_executor")
    private User executor;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime deadline;

    public Task(String title, String description, TaskStatus status, TaskPriority priority, User creator, User executor, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.creator = creator;
        this.executor = executor;
        this.deadline = deadline;
    }

    public Task() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", creatorId=" + (creator == null ? null : creator.getId()) +
                ", executorId=" + (executor == null ? null : executor.getId()) +
                ", createdAt=" + createdAt +
                ", deadline=" + deadline +
                '}';
    }

}
