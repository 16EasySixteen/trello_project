package com.example.trelloproject.user.dto;

import com.example.trelloproject.user.enumclass.UserRole;
import lombok.Getter;

@Getter
public class UserSignUpRequestDto {
    private String name;
    private String email;
    private String password;
    private String userRole;
}
