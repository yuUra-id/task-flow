package com.sharko.yura.taskflow.dto;

import com.sharko.yura.taskflow.entity.TaskPriority;
import com.sharko.yura.taskflow.entity.TaskStatus;

import java.time.LocalDateTime;

public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String creatorUsername;
    private String executorUsername;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime deadline;
    private LocalDateTime creationDate;

    public TaskResponseDTO() {}

    public TaskResponseDTO(Long id, String title, String description,
                           String creatorUsername, String executorUsername,
                           TaskPriority priority, TaskStatus status, LocalDateTime deadline,
                           LocalDateTime creationDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creatorUsername = creatorUsername;
        this.executorUsername = executorUsername;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
        this.creationDate = creationDate;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorUsername(String usernameCreator) {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public String getExecutorUsername() {
        return executorUsername;
    }

    public void setExecutorUsername(String executorUsername) {
        this.executorUsername = executorUsername;
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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "TaskResponseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creatorUsername='" + creatorUsername + '\'' +
                ", executorUsername='" + executorUsername + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", deadline=" + deadline +
                ", creationDate=" + creationDate +
                '}';
    }

}
