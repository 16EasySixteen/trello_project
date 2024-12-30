package com.example.trelloproject.user.dto;

import com.example.trelloproject.user.enumclass.UserRole;
import lombok.Getter;

@Getter
public class UserSignUpRequestDto {
    private String name;
    private String email;
    private String password;
    private String userRole;

    public UserSignUpRequestDto(String name, String email, String password, String userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
}
