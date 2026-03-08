package com.sharko.yura.taskflow.security;

import com.sharko.yura.taskflow.config.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр аутентификации JWT.
 * Данный фильтр перехватывает каждый входящий HTTP-запрос и проверяет наличие
 * JWT токена в заголовке Authorization. Если токен присутствует и является
 * валидным, фильтр извлекает информацию о пользователе и устанавливает
 * объект аутентификации в контекст безопасности Spring Security.
 * Основные задачи фильтра:
 * 1. Получить JWT токен из заголовка Authorization.
 * 2. Проверить корректность формата заголовка (Bearer token).
 * 3. Извлечь имя пользователя из JWT токена.
 * 4. Загрузить пользователя из базы данных.
 * 5. Проверить валидность токена (подпись, срок действия, соответствие пользователю).
 * 6. Создать объект Authentication.
 * 7. Сохранить объект Authentication в SecurityContext.
 * Фильтр наследуется от OncePerRequestFilter, что гарантирует выполнение
 * фильтра только один раз для каждого HTTP-запроса.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService){

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;

    }

     /**
     * Основной метод фильтра.
     * Выполняется для каждого HTTP-запроса и выполняет следующие действия:
     * 1. Получает заголовок Authorization из запроса.
     * 2. Проверяет наличие JWT токена в формате "Bearer token".
     * 3. Извлекает токен из заголовка.
     * 4. Получает имя пользователя из JWT токена.
     * 5. Проверяет, не аутентифицирован ли пользователь уже.
     * 6. Загружает пользователя из базы данных.
     * 7. Проверяет валидность токена.
     * 8. Создает объект Authentication и сохраняет его в SecurityContext.
     * 9. Передает запрос дальше по цепочке фильтров.
     *
     * @param request HTTP-запрос клиента
     * @param response HTTP-ответ сервера
     * @param filterChain цепочка фильтров безопасности
     *
     * @throws ServletException если произошла ошибка сервлета
     * @throws IOException если произошла ошибка ввода/вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //получили заголовок из запроса
        final String authorizationHeader = request.getHeader("Authorization");
        //проверка на наличие токена
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;

        }
        //удаляем из заголовка "Bearer "
        String jwtToken = authorizationHeader.substring(7);
        //извлекаем username из токена
        String username = jwtService.extractUsername(jwtToken);

        //проверка существования пользователя и проверка, что он ещё не аутентифицирован
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Получаем пользователя из бд
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtService.isAccessTokenValid(jwtToken, userDetails)) {
                //Создаем объект аутентификации пользователя.
                //Он содержит информацию о пользователе и его ролях.
                UsernamePasswordAuthenticationToken authenticationToken =
                        //создается токен аутентификации
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                //сохраняем в контекст Security
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

        }

        filterChain.doFilter(request, response);

    }

}
