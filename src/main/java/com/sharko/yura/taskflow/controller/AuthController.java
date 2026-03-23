package com.sharko.yura.taskflow.controller;

import com.sharko.yura.taskflow.auth.AuthResponseDTO;
import com.sharko.yura.taskflow.auth.LoginRequestDTO;
import com.sharko.yura.taskflow.auth.RefreshTokenRequestDTO;
import com.sharko.yura.taskflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {

        log.info("AUTH API: login request for user {}", loginRequestDTO.getUsername());

        return authService.login(loginRequestDTO);

    }

    @PostMapping("/refresh")
    public AuthResponseDTO refresh(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {

        log.info("AUTH API: refresh token request");

        return authService.refreshToken(refreshTokenRequestDTO.getRefreshToken());

    }

}
