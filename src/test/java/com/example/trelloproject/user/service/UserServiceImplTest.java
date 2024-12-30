package com.example.trelloproject.user.service;

import com.example.trelloproject.notification.NotificationService;
import com.example.trelloproject.user.dto.UserSignUpRequestDto;
import com.example.trelloproject.user.dto.UserSignUpResponseDto;
import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.enumclass.UserRole;
import com.example.trelloproject.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_whenUserExists() {
        UserSignUpRequestDto dto = new UserSignUpRequestDto("abc", "abc@gmail.com", "abcd1234", "ADMIN");
        User user = new User("abc", "abc@gmail.com", "abcd1234", UserRole.ADMIN);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    void createUser_whenUserNotExists() {
        UserSignUpRequestDto dto = new UserSignUpRequestDto("abc", "abc@gmail.com", "abcd1234", "ADMIN");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        UserSignUpResponseDto responseDto = userService.createUser(dto);

        assertNotNull(responseDto);
        assertThat(responseDto.getName()).isEqualTo(dto.getName());
        assertThat(responseDto.getEmail()).isEqualTo(dto.getEmail());
        assertThat(responseDto.getUserRole()).isEqualTo(UserRole.ADMIN);
    }
}