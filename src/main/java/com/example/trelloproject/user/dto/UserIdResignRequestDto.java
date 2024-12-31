package com.example.trelloproject.user.dto;

import lombok.Getter;

@Getter
public class UserIdResignRequestDto {
    private String email;
    private String password;
    private String checkedPassword;
}
