package com.sharko.yura.taskflow.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO объект ответа при успешной аутентификации пользователя.
 * Используется для передачи JWT токенов клиенту после успешного логина.
 * В ответе возвращаются два типа токенов:
 * 1. Access Token
 *    Используется для доступа к защищенным API эндпоинтам.
 *    Передается клиентом в HTTP заголовке:
 *    Authorization: Bearer <accessToken>
 *    Access токен имеет короткий срок жизни.
 * 2. Refresh Token
 *    Используется для получения нового access токена
 *    после истечения срока действия текущего.
 *    Refresh токен имеет более долгий срок жизни.
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {

    private String accessToken;
    private String refreshToken;

}
