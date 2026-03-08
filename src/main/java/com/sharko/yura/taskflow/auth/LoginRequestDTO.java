package com.sharko.yura.taskflow.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO объект запроса аутентификации пользователя.
 * Используется для передачи учетных данных пользователя
 * от клиента к серверу при выполнении операции входа.
 * Содержит:
 * username - имя пользователя (логин)
 * password - пароль пользователя
 */
@Getter
@Setter
public class LoginRequestDTO {

    private String username;
    private String password;

}
