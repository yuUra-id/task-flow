package com.sharko.yura.taskflow.controller;

import com.sharko.yura.taskflow.dto.UserCreateDTO;
import com.sharko.yura.taskflow.dto.UserResponseDTO;
import com.sharko.yura.taskflow.dto.UserUpdateDTO;
import com.sharko.yura.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {

        List<UserResponseDTO> users = userService.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {

        UserResponseDTO userResponseDTO = userService.findById(id);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO  userUpdateDTO) {

        UserResponseDTO userResponseDTO = userService.update(id, userUpdateDTO);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        userService.delete(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/username")
    public ResponseEntity<UserResponseDTO>  getByUsername(@RequestParam String username) {

        UserResponseDTO userResponseDTO = userService.findByUsername(username);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseDTO> getByEmail(@RequestParam String email) {

        UserResponseDTO userResponseDTO = userService.findByEmail(email);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);

    }

}
