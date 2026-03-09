package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.auth.AuthResponseDTO;
import com.sharko.yura.taskflow.auth.LoginRequestDTO;
import com.sharko.yura.taskflow.config.CustomUserDetailsService;
import com.sharko.yura.taskflow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Сервис аутентификации пользователей.
 * Данный сервис отвечает за процесс входа пользователя
 * в систему и генерацию JWT токенов.
 * 1. Проверка учетных данных пользователя (username и password)
 *    через AuthenticationManager Spring Security.
 * 2. Получение информации о пользователе (UserDetails)
 *    после успешной аутентификации.
 * 3. Извлечение роли пользователя из списка authorities.
 * 4. Генерация JWT токенов:
 * 5. Возврат токенов клиенту в виде AuthResponseDTO.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    /**
     * Выполняет аутентификацию пользователя.
     * Метод принимает логин и пароль пользователя,
     * передает их в AuthenticationManager Spring Security
     * для проверки.
     * Если учетные данные корректны:
     * - извлекается информация о пользователе
     * - определяется его роль
     * - генерируются JWT токены
     * @param loginRequestDTO объект запроса содержащий
     * username и password пользователя
     * @return AuthResponseDTO содержащий
     * accessToken и refreshToken
     */
    public AuthResponseDTO login(LoginRequestDTO  loginRequestDTO) {

        log.info("AUTH: login attempt for user {}", loginRequestDTO.getUsername());

        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )

        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("AUTH: user {} authenticated successfully", userDetails.getUsername());

        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        log.debug("AUTH: user {} has role {}", userDetails.getUsername(), role);

        log.debug("AUTH: generate token for user {}", userDetails.getUsername());
        String accessToken = jwtService.generateAccessToken(userDetails.getUsername(), role);
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        log.info("AUTH: user {} logged in successfully", userDetails.getUsername());
        return new AuthResponseDTO(accessToken, refreshToken);

    }

    public AuthResponseDTO refreshToken(String refreshToken) {

        log.info("AUTH: refresh token request received");

        String username = jwtService.extractUsername(refreshToken);
        log.debug("AUTH: refresh token belongs to user {}", username);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(!jwtService.isRefreshTokenValid(refreshToken, userDetails)) {

            log.warn("AUTH: invalid refresh token for user {}", username);
            throw new RuntimeException("Invalid Refresh Token");

        }

        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        log.debug("AUTH: user {} has role {}", userDetails.getUsername(), role);

        String newAccessToken = jwtService.generateAccessToken(username, role);

        String newRefreshToken = jwtService.generateRefreshToken(username);
        log.info("AUTH: new token issued for user {}", username);

        return new AuthResponseDTO(newAccessToken, newRefreshToken);

    }

}
