package com.sharko.yura.taskflow.controller;

import com.sharko.yura.taskflow.auth.AuthResponseDTO;
import com.sharko.yura.taskflow.auth.LoginRequestDTO;
import com.sharko.yura.taskflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {

        return authService.login(loginRequestDTO);

    }

}
