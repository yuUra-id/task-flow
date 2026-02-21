package com.sharko.yura.taskflow.dto;

import com.sharko.yura.taskflow.entity.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import static com.sharko.yura.taskflow.entity.TaskPriority.LOW;

public class TaskCreateDTO {

    @NotBlank(message = "Title не может быть пустым")
    @Size(min = 3, max = 50, message = "Title должен быть от 3 до 50 символов")
    private String title;

    @NotBlank(message = "Description не может быть пустым")
    @Size(min = 3, max = 500, message = "Description должен быть от 3 до 500 символов")
    private String description;

    private Long executorId;

    private TaskPriority priority = TaskPriority.LOW;

    @NotNull(message = "Deadline не может быть пустым")
    private LocalDateTime deadline;

    public TaskCreateDTO() {}

    public TaskCreateDTO(String title, String description, Long executorId, TaskPriority priority, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.executorId = executorId;
        this.priority = priority;
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

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
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

    @Override
    public String toString() {
        return "TaskCreateDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", executorId=" + executorId +
                ", priority=" + priority +
                ", deadline=" + deadline +
                '}';
    }

}
