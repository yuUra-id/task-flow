package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.UserCreateDTO;
import com.sharko.yura.taskflow.dto.UserResponseDTO;
import com.sharko.yura.taskflow.dto.UserUpdateDTO;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Обеспечивает операции создания, получения, обновления и удаления пользователей,
 * а также поиск по имени и email. Все методы возвращают DTO для безопасного обмена
 * данными между слоями приложения.
 */
public interface UserService {

    //Получить список всех пользователей.
    List<UserResponseDTO> findAll();

    //Найти пользователя по его уникальному идентификатору.
    UserResponseDTO findById(Long id);

    //Создать нового пользователя.
    UserResponseDTO create(UserCreateDTO userCreateDTO);

    //Обновить существующего пользователя.
    UserResponseDTO update(Long id, UserUpdateDTO dto);

    //Удалить пользователя по его id.
    void delete(Long id);

    //Найти пользователя по имени.
    UserResponseDTO findByUsername(String username);

    //Найти пользователя по email.
    UserResponseDTO findByEmail(String email);

}
