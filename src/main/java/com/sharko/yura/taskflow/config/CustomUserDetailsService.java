package com.sharko.yura.taskflow.config;

import com.sharko.yura.taskflow.entity.User;
import com.sharko.yura.taskflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Реализация интерфейса UserDetailsService, используемая Spring Security
 * для загрузки пользователя из базы данных во время процесса аутентификации.
 * Во время логина Spring Security вызывает метод
 * loadUserByUsername(String), который:
 * Ищет пользователя в базе данных по username
 * Если пользователь не найден выбрасывает UsernameNotFoundException.
 * Преобразует сущность в объект UserDetails.
 * Передает Spring Security данные для аутентификации
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        log.info("SECURITY: attempt load user with username {}", username);

        log.debug("SECURITY: searching user in database by username {}", username);
        User user = userRepository.findByUsername(username);

        if (user == null) {

            log.warn("SECURITY: user with {} not found", username);
            throw new UsernameNotFoundException(username);

        }

        log.debug("SECURITY: user {} found with role {}", username, user.getRole());

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        log.info("SECURITY: user details successfully loaded for {}", username);
        return userDetails;
    }
}
