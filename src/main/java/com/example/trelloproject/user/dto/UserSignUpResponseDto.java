package com.example.trelloproject.user.dto;

import com.example.trelloproject.user.enumclass.UserRole;
import lombok.Getter;

@Getter
public class UserSignUpResponseDto {
    private String name;
    private String email;
    private UserRole userRole;

    public UserSignUpResponseDto(String name, String email, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.userRole = userRole;
    }
}
