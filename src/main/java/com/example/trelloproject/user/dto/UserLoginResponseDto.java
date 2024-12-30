package com.example.trelloproject.user.dto;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {
    private String message;
    private String name;
    private String email;
    private String accessToken;

    public UserLoginResponseDto(String message, String name, String email, String accessToken) {
        this.message = message;
        this.name = name;
        this.email = email;
        this.accessToken = accessToken;
    }
}
