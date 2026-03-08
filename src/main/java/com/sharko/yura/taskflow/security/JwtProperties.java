package com.sharko.yura.taskflow.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Класс конфигурации параметров JWT.
 * Данный класс используется для загрузки настроек JWT
 * из конфигурационного файла приложения application.properties.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;

}
