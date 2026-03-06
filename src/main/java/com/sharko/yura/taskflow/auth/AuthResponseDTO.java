package com.sharko.yura.taskflow.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {

    private String accessToken;
    private String refreshToken;

}
