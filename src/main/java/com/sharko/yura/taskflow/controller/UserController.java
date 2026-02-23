package com.sharko.yura.taskflow.controller;

import com.sharko.yura.taskflow.dto.UserCreateDTO;
import com.sharko.yura.taskflow.dto.UserResponseDTO;
import com.sharko.yura.taskflow.dto.UserUpdateDTO;
import com.sharko.yura.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления пользователями.
 * Обрабатывает HTTP-запросы, связанные с сущностью User.
 * Реализует стандартные CRUD-операции:
 * Создание пользователя
 * Получение списка пользователей
 * Получение пользователя по ID
 * Обновление данных пользователя
 * Удаление пользователя
 * Поиск по username и email
 * Все ответы возвращаются в виде DTO-объектов, что исключает утечку чувствительных данных (например, пароля).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;

    }
    //Создание пользователя
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO userCreateDTO) {

        UserResponseDTO userResponseDTO = userService.create(userCreateDTO);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);

    }
    //Получение списка пользователей
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponseDTO>> getAll() {

        List<UserResponseDTO> users = userService.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);

    }
    //Получение пользователя по ID
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {

        UserResponseDTO userResponseDTO = userService.findById(id);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }
    //Обновление данных пользователя
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO  userUpdateDTO) {

        UserResponseDTO userResponseDTO = userService.update(id, userUpdateDTO);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }
    //Удаление пользователя
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        userService.delete(id);

        return ResponseEntity.noContent().build();

    }
    //Поиск по username
    @GetMapping("/username")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO>  getByUsername(@RequestParam String username) {

        UserResponseDTO userResponseDTO = userService.findByUsername(username);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }
    //Поиск по email
    @GetMapping("/email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> getByEmail(@RequestParam String email) {

        UserResponseDTO userResponseDTO = userService.findByEmail(email);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }

}
