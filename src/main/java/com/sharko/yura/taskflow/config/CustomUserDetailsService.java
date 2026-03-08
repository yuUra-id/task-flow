package com.sharko.yura.taskflow.config;

import com.sharko.yura.taskflow.entity.User;
import com.sharko.yura.taskflow.repository.UserRepository;
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

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
