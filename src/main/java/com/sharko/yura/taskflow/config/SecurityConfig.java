package com.sharko.yura.taskflow.config;

import com.sharko.yura.taskflow.security.JwtAuthenticationFilter;
import com.sharko.yura.taskflow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурационный класс безопасности приложения.
 * Настраивает Spring Security для работы с JWT аутентификацией.
 * Основные функции:
 * - отключение CSRF
 * - stateless аутентификация
 * - подключение JWT фильтра
 * - настройка доступа к API
 * - конфигурация PasswordEncoder
 * - предоставление AuthenticationManager
 */
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Создает JWT фильтр для проверки токенов
     * в каждом входящем HTTP запросе.
     * @return JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {

        return new JwtAuthenticationFilter(jwtService, customUserDetailsService);

    }

    /**
     * Основная конфигурация безопасности HTTP.
     * - отключение CSRF
     * - stateless сессии
     * - разрешение доступа к /api/auth/**
     * - аутентификация для остальных запросов
     * - подключение JWT фильтра
     * @param http объект конфигурации безопасности
     * @return SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http    //отключаем защиту CSRF
                .csrf(csrf -> csrf.disable())
                //отключаем создание HTTP сессии данные будут храниться не в сессии ,а в токене.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Настройка правил авторизации
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Кодировщик паролей BCrypt.
     * Используется для безопасного хранения
     * паролей пользователей в базе данных.
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    /**
     * Предоставляет AuthenticationManager
     * для выполнения аутентификации пользователя.
     * Используется при логине.
     * @param configuration конфигурация аутентификации
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();

    }

}