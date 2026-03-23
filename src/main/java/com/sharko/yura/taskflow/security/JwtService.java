package com.sharko.yura.taskflow.security;

import com.sharko.yura.taskflow.controller.UserController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Сервис работы с JWT токенами.
 * Данный сервис отвечает за:
 * - генерацию access token
 * - генерацию refresh token
 * - извлечение данных из токенов
 * - проверку валидности токенов
 */
@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    public JwtService(JwtProperties jwtProperties) {

        this.jwtProperties = jwtProperties;

    }

    /**
     * Генерируем короткоживущий токен
     * @param username имя пользователя
     * @return JWT токен в виде строки
     */
    public String generateAccessToken(String username, String role) {

        log.debug("JWT: generating access token for user {} with role {}", username, role);
        return generateToken(username, role, "access", jwtProperties.getAccessTokenExpiration());

    }

    /**
     * Извлекаем имя из токена
     * @param token JWT токен
     * @return имя пользователя (subject)
     */
    public String extractUsername(String token) {

        log.debug("JWT: extracting username from token");
        String username = getClaimsFromToken(token).getSubject();

        log.debug("JWT: username {} extracted from token", username);
        return username;

    }

    /**
     * Генерируем долгоживущий токен
     * @param username имя пользователя
     * @return JWT токен в виде строки
     */
    public String generateRefreshToken(String username) {

        log.debug("JWT: generating refresh token for user {}", username);
        return generateToken(username, null, "refresh", jwtProperties.getRefreshTokenExpiration());

    }

    /**
     * Извлекает роль пользователя из токена.
     * @param token JWT токен
     * @return роль пользователя
     */
    public String extractRole(String token) {

        log.debug("JWT: extracting role from token");
        String role = getClaimsFromToken(token).get("role", String.class);

        log.debug("JWT: role {} extracted from token", role);
        return role;

    }

    /**
     * Извлекает тип токена (access или refresh).
     * @param token JWT токен
     * @return тип токена
     */
    public String extractTokenType(String token) {

        log.debug("JWT: extracting type from token");
        String tokenType = getClaimsFromToken(token).get("type", String.class);

        log.debug("JWT: token type {} extracted", tokenType);
        return tokenType;

    }

    /**
     * Проверяет валидность access token.
     * Проверка через общий метод isTokenValid
     * @param token JWT токен
     * @param userDetails данные пользователя
     * @return true если токен валиден
     */
    public boolean isAccessTokenValid(String token, UserDetails userDetails) {

        log.debug("JWT: validating access token for user {}", userDetails.getUsername());
        return isTokenValid(token, userDetails, "access");

    }

    /**
     * Проверяет валидность refresh token.
     * @param token JWT токен
     * @param userDetails данные пользователя
     * @return true если токен валиден
     */
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {

        log.debug("JWT: validating refresh token for user {}", userDetails.getUsername());
        return isTokenValid(token, userDetails, "refresh");

    }

    /**
     * Универсальный метод проверки токена.
     * Метод проверяет:
     * совпадает ли username в токене с пользователем
     * совпадает ли тип токена
     * не истек ли срок действия токена
     * @param token JWT токен
     * @param userDetails пользователь
     * @param expectedType ожидаемый тип токена
     * @return true если токен валиден
     */
    private boolean isTokenValid(String token, UserDetails userDetails, String expectedType) {

        try {

            final String username = extractUsername(token);
            final String type = extractTokenType(token);

            return username.equals(userDetails.getUsername())
                    && type.equals(expectedType)
                    && !isTokenExpired(token);

        }catch (JwtException e) {

            return false;

        }

    }

    /**
     * Базовый метод для генерации JWT токена
     * @param username имя пользователя
     * @param expiration время жизни токена
     * @return сгенерированный JWT токен
     */
    private String generateToken(String username, String role, String type, long expiration) {

        log.debug("JWT: building {} token for user {}", type, username);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(username)
                .claim("type", type)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey());

        if(role != null) {
            log.debug("JWT: adding role {} to {} token for user {}", role, type, username);
            jwtBuilder.claim("role", role);
        }

        String token = jwtBuilder.compact();

        log.debug("JWT: {} token successfully generated for user {}", type, username);
        return token;

    }

    /**
     * Создает ключ для подписи JWT из секрета в конфигурации.
     * @return SecretKey для подписи
     */
    private SecretKey getSignKey(){
        log.debug("JWT: generating signing key from configured seecret");
        //Берем ключ из конфигурации (Base64 строка)
        String secretKey = jwtProperties.getSecret();
        //Декодируем в массив байт
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        //Создаем ключ
        return Keys.hmacShaKeyFor(keyBytes);

    }

    /**
     * Извлекает все данные хранящиеся в payload части токена.(claims)
     * @param token JWT токен
     * @return Claims со всеми данными токена
     */
    private Claims getClaimsFromToken(String token) {

        log.debug("JWT: parsing token claims");
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token).getBody();

    }

    /**
     * Проверяем истек токен или нет
     * @param token JWT токен
     * @return true если токен истек, false если еще действителен
     */
    private boolean isTokenExpired(String token) {

        boolean expired = getClaimsFromToken(token)
                .getExpiration()
                .before(new Date());

        log.debug("JWT: token expiration check result = {}", expired);
        return expired;
    }

}
