package com.example.trelloproject.user.service;

import com.example.trelloproject.user.dto.*;
import org.springframework.dao.DuplicateKeyException;

public interface UserService {

    UserSignUpResponseDto createUser(UserSignUpRequestDto userSignUpRequestDto) throws DuplicateKeyException;

    UserLoginResponseDto loginUser(UserLoginRequestDto userLoginRequestDto);

    UserIdResignResponseDto resignUser(UserIdResignRequestDto userIdResignRequestDto);
}
