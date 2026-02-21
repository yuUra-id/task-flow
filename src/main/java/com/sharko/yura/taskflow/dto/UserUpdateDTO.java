package com.sharko.yura.taskflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * DTO для обновления данных пользователя.
 * Используется в PUT-запросах при обновлении информации о пользователе.
 * Содержит только изменяемые поля (username и email).
 * Пароль не обновляется через данный DTO.
 * Валидация выполняется с помощью Jakarta Validation
 * перед передачей данных в сервисный слой.
 */
public class UserUpdateDTO {

    /**
     * Логин пользователя.
     * Не может быть пустым, содержит только буквы, цифры и _,
     * должен быть уникальным
     */
    @NotBlank(message = "Username не может быть пустым")
    @Size(min = 3, max = 50, message = "Username должен быть от 3 до 50 символов")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username может содержать только буквы, цифры и _")
    private String username;

    /**
     * Email пользователя
     * Должен быть уникальным и соответствовать формату email, не может быть пустым
     */
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String username, String email) {
        this.username = username;
        this.email = email;
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

    @Override
    public String toString() {
        return "UserUpdateDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
