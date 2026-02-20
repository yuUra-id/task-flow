package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.UserCreateDTO;
import com.sharko.yura.taskflow.dto.UserResponseDTO;
import com.sharko.yura.taskflow.dto.UserUpdateDTO;
import com.sharko.yura.taskflow.entity.User;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> findAll();

    UserResponseDTO findById(Long id);

    UserResponseDTO create(UserCreateDTO userCreateDTO);

//    User save(UserCreateDTO userCreateDTO);

    UserResponseDTO update(Long id, UserUpdateDTO dto);

    void delete(Long id);

    UserResponseDTO findByUsername(String username);

    UserResponseDTO findByEmail(String email);

}
