package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.UserCreateDTO;
import com.sharko.yura.taskflow.dto.UserResponseDTO;
import com.sharko.yura.taskflow.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    UserResponseDTO create(UserCreateDTO userCreateDTO);

//    User save(UserCreateDTO userCreateDTO);

    User update(User user);

    void delete(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

}
