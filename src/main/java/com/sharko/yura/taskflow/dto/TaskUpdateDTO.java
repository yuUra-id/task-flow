package com.sharko.yura.taskflow.dto;

import com.sharko.yura.taskflow.entity.TaskPriority;
import com.sharko.yura.taskflow.entity.TaskStatus;
import com.sharko.yura.taskflow.entity.User;

import java.time.LocalDateTime;

public class TaskUpdateDTO {

    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long executorID;
    private LocalDateTime deadline;

    public TaskUpdateDTO() {}

    public TaskUpdateDTO(String title, String description, TaskStatus status, TaskPriority priority,
                         Long executorID, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.executorID = executorID;
        this.deadline = deadline;
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

    public Long getExecutorID() {
        return executorID;
    }

    public void setExecutorID(Long executorID) {
        this.executorID = executorID;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

}
