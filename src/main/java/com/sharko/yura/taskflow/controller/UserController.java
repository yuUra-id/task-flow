package com.sharko.yura.taskflow.controller;

import com.sharko.yura.taskflow.dto.UserCreateDTO;
import com.sharko.yura.taskflow.dto.UserResponseDTO;
import com.sharko.yura.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;

    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO userCreateDTO) {

        UserResponseDTO userResponseDTO = userService.create(userCreateDTO);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);

    }

}
