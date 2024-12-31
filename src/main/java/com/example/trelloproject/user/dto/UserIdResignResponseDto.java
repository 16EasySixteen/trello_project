package com.example.trelloproject.user.dto;

import lombok.Getter;

@Getter
public class UserIdResignResponseDto {
    private String message;
    private String email;

    public UserIdResignResponseDto(String message, String email) {
        this.message = message;
        this.email = email;
    }
}
