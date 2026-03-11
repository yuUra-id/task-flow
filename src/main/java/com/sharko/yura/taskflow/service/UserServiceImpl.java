package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.UserCreateDTO;
import com.sharko.yura.taskflow.dto.UserResponseDTO;
import com.sharko.yura.taskflow.dto.UserUpdateDTO;
import com.sharko.yura.taskflow.entity.User;
import com.sharko.yura.taskflow.exception.PasswordMismatchException;
import com.sharko.yura.taskflow.exception.UserAlreadyExistsException;
import com.sharko.yura.taskflow.exception.UserNotFoundException;
import com.sharko.yura.taskflow.exception.UserWithEmailAlreadyExistsException;
import com.sharko.yura.taskflow.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация сервиса UserService для управления пользователями.
 * Обеспечивает полный набор CRUD-операций:
 * Создание пользователя с хешированием пароля
 * Получение пользователя по id, username, email
 * Обновление данных пользователя с проверкой уникальности
 * Удаление пользователя
 * Все операции возвращают DTO для безопасного обмена данными с контроллером.
 * Также реализована валидация и обработка ошибок:
 * Проверка уникальности username и email
 * Проверка подтверждения пароля при создании пользователя
 * Генерация исключений UserNotFoundException, UserAlreadyExistsException,
 * UserWithEmailAlreadyExistsException, PasswordMismatchException
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    public UserServiceImpl(UserRepository userRepository,  PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    //Получение списка всех пользователей
    @Override
    public List<UserResponseDTO> findAll() {

        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        for (User user : users) {
            UserResponseDTO userResponseDTO = mapToDTO(user);
            userResponseDTOS.add(userResponseDTO);
        }

        return userResponseDTOS;

    }

    //Получение пользователя по id
    @Override
    public UserResponseDTO findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        return mapToDTO(user);

    }

    //Создание пользователя с хешированием пароля
    @Override
    @Transactional
    public UserResponseDTO create(UserCreateDTO userCreateDTO) {

        log.info("USER: attempt create user with name {}", userCreateDTO.getUsername());

        log.debug("USER: checking passwords when creating a user (password and passwordConfirm)");
        if(!userCreateDTO.isPasswordConfirmed()){

            log.warn("USER: passwords don't match");
            throw new PasswordMismatchException("Mismatch password");

        }

        log.debug("USER: checking exists user with name {}", userCreateDTO.getUsername());
        if(userRepository.existsByUsername(userCreateDTO.getUsername())){

            log.warn("USER: user with name {} already exists", userCreateDTO.getUsername());
            throw new UserAlreadyExistsException("User with name " + userCreateDTO.getUsername() + " already exists");

        }

        log.debug("USER: checking exists user with email {}", userCreateDTO.getEmail());
        if(userRepository.existsByEmail(userCreateDTO.getEmail())){

            log.warn("USER: user with email {} already exists", userCreateDTO.getEmail());
            throw new UserWithEmailAlreadyExistsException("User with email " + userCreateDTO.getEmail() + " already exists");

        }

        log.debug("USER: encoding password");
        String encodedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        log.debug("USER: creating a new user with name {}", userCreateDTO.getUsername());
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(userCreateDTO.getRole());

        log.debug("USER: saving new user");
        User saveUser = userRepository.save(user);

        log.info("USER: new user with name {} successfully created and saved", userCreateDTO.getUsername());
        return mapToDTO(saveUser);
    }

    //Обновление данных пользователя с проверкой уникальности
    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {

        log.info("USER: attempt update user with name {}", dto.getUsername());

        log.debug("USER: checking exists user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id " + id + " not found"));

        log.debug("USER: find user with username {}", dto.getUsername());
        User userByUsername = userRepository.findByUsername(dto.getUsername());

        if(userByUsername!=null && !userByUsername.getId().equals(id)){

            throw new UserAlreadyExistsException("User with name " + dto.getUsername() + " already exists");

        }

        log.debug("USER: find user with email {}", dto.getEmail());
        User userByEmail = userRepository.findByEmail(dto.getEmail());
        
        if(userByEmail!=null && !userByEmail.getId().equals(id)){

            throw new UserWithEmailAlreadyExistsException("User with email " + dto.getEmail() + " already exists");

        }

        log.debug("USER: update username for user with ID: {}", id);
        user.setUsername(dto.getUsername());
        log.debug("USER: update email for user with ID: {}", id);
        user.setEmail(dto.getEmail());
        log.debug("USER: saving username and email for user with ID {}", id);
        userRepository.save(user);

        log.info("USER: user with ID {} successfully creating and saving", id);
        return mapToDTO(user);

    }

    //Удаление пользователя
    @Override
    @Transactional
    public void delete(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        userRepository.delete(user);

    }

    //Получение пользователя по username
    @Override
    public UserResponseDTO findByUsername(String username) {

        User user = userRepository.findByUsername(username);
        if(user==null){
            throw new UserNotFoundException("User with name " + username + " not found");
        }

        return mapToDTO(user);

    }

    //Получение пользователя по email
    @Override
    public UserResponseDTO findByEmail(String email) {

        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new UserNotFoundException("User with email " + email + " not found");
        }

        return mapToDTO(user);

    }

    //Маппинг User in DTO
    private UserResponseDTO mapToDTO(User user){

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setCreatedAt(user.getCreatedAt());

        return userResponseDTO;

    }

}
