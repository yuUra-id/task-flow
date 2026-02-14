package com.sharko.yura.taskflow.dto;

import com.sharko.yura.taskflow.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * DTO для создания нового пользователя
 * используется при регистрации нового пользователя или создания админом
 * Поля используются только те, которые требуются для создания пользователя
 * так же реализована валидация
 */
public class UserCreateDTO {

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

    /**
     * Role пользователя
     * по умолчанию стоит USER, но admin может передать другую роль
     */
    private Role role = Role.USER;

    /**
     * Пароль пользователя
     * будет хэшироваться перед сохранением, не может быть пустым
     */
    @NotBlank(message = "Password Не может быть пустым")
    @Size(min = 6, max = 255, message = "Password должен быть от 6 до 255 символов")
    private String password;

    /**
     * Подтверждение пароля
     * Для проверки корректности ввода пароля, не может быть пустым
     */
    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    @Size(min = 6, max = 255, message = "Подтверждение пароля должно быть от 6 до 255 символов")
    private String passwordConfirm;

    public UserCreateDTO() {}

    public UserCreateDTO(String username, String email, Role role, String password, String passwordConfirm) {
        this.username = username;
        this.email = email;
        this.role = role == null? Role.USER : role;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    //Метод для проверки совпадения пароля и подтверждения пароля
    public boolean isPasswordConfirmed(){
        return password.equals(passwordConfirm);
    }

}
