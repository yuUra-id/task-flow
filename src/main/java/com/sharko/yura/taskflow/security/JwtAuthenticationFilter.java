package com.sharko.yura.taskflow.security;

import com.sharko.yura.taskflow.config.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

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

        log.debug("SECURITY: processing request {}, {}",request.getMethod(),request.getRequestURI());
        //получили заголовок из запроса
        final String authorizationHeader = request.getHeader("Authorization");
        //проверка на наличие токена
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            log.debug("SECURITY: no JWT token found");
            filterChain.doFilter(request, response);
            return;

        }
        //удаляем из заголовка "Bearer "
        String jwtToken = authorizationHeader.substring(7);
        log.debug("SECURITY: JWT token extracted");
        String username;
        try {
            //извлекаем username из токена
            username = jwtService.extractUsername(jwtToken);
            log.debug("SECURITY: token belongs to user {}", username);
        }catch (Exception e){

            log.warn("SECURITY: JWT token extract error");
            filterChain.doFilter(request, response);
            return;

        }

        //проверка существования пользователя и проверка, что он ещё не аутентифицирован
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Получаем пользователя из бд
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.debug("SECURITY: loading user details for user {}", username);

            if(jwtService.isAccessTokenValid(jwtToken, userDetails)) {
                log.debug("SECURITY: validating JWT token for user {}", username);
                //Создаем объект аутентификации пользователя.
                //Он содержит информацию о пользователе и его ролях.
                UsernamePasswordAuthenticationToken authenticationToken =
                        //создается токен аутентификации
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                //сохраняем в контекст Security
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("SECURITY: user {} authenticated via JWT", username);

            }else {
                log.warn("SECURITY: invalid JWT token for user {}", username);
            }

        }

        filterChain.doFilter(request, response);

    }

}
