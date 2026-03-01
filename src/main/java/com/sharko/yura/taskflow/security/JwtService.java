package com.sharko.yura.taskflow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Генерируем короткоживущий токен
     * @param username имя пользователя
     * @return JWT токен в виде строки
     */
    public String generateAccessToken(String username) {

        return generateToken(username, jwtProperties.getAccessTokenExpiration());

    }

    /**
     * Извлекаем имя из токена
     * @param token JWT токен
     * @return имя пользователя (subject)
     */
    public String getUsername(String token) {

        return getClaimsFromToken(token).getSubject();

    }

    /**
     * Генерируем долгоживущий токен
     * @param username имя пользователя
     * @return JWT токен в виде строки
     */
    public String generateRefreshToken(String username) {

        return generateToken(username, jwtProperties.getRefreshTokenExpiration());

    }

    /**
     * Проверяет, валиден ли токен для данного пользователя.
     * @param token JWT токен
     * @param userDetails данные пользователя из Spring Security
     * @return true если токен валиден, иначе false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {

        final String username = getUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    /**
     * Базовый метод для генерации JWT токена
     * @param username имя пользователя
     * @param expiration время жизни токена
     * @return сгенерированный JWT токен
     */
    private String generateToken(String username, long expiration) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();

    }

    /**
     * Создает ключ для подписи JWT из секрета в конфигурации.
     * @return SecretKey для подписи
     */
    private SecretKey getSignKey(){
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

        return getClaimsFromToken(token)
                .getExpiration()
                .before(new Date());

    }

}
