package com.example.trelloproject.user.service;

import com.example.trelloproject.notification.NotificationService;
import com.example.trelloproject.user.config.auth.UserDetailsImpl;
import com.example.trelloproject.user.dto.*;
import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.enumclass.UserRole;
import com.example.trelloproject.user.enumclass.UserStatus;
import com.example.trelloproject.user.repository.UserRepository;
import com.example.trelloproject.user.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final NotificationService notificationService;

    //회원가입
    @Override
    @Transactional
    public UserSignUpResponseDto createUser(UserSignUpRequestDto userSignUpRequestDto) throws DuplicateKeyException {

        boolean dup = this.userRepository.findByEmail(userSignUpRequestDto.getEmail()).isPresent();

        if(dup) {
            throw new DuplicateKeyException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());
        User user = new User(
                userSignUpRequestDto.getName(),
                userSignUpRequestDto.getEmail(),
                encodedPassword,
                UserRole.of(userSignUpRequestDto.getUserRole()));

        userRepository.save(user);

        notificationService.sendMessageToSlack("회원가입 성공");

        return new UserSignUpResponseDto(user.getName(), user.getEmail(), user.getUserRole());
    }

    //로그인
    @Override
    public UserLoginResponseDto loginUser(UserLoginRequestDto userLoginRequestDto) {

        User user = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
        validatePassword(userLoginRequestDto.getPassword(), user.getPassword());

        if (user.getUserStatus().equals(UserStatus.DELETED)) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword())
        );
        log.info("SecurityContext에 Authentication 저장.");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateToken(authentication);
        log.info("토큰 생성: {}", accessToken);

        // 알람 메시지
        notificationService.sendMessageToSlack("로그인 성공");

        return new UserLoginResponseDto("로그인 성공", user.getName(), user.getEmail(), accessToken);
    }

    //회원탈퇴
    @Override
    public UserIdResignResponseDto resignUser(UserIdResignRequestDto userIdResignRequestDto) {
        User user = getSessionUser();

        if (!userIdResignRequestDto.getPassword().equals(userIdResignRequestDto.getCheckedPassword())) {
            throw new IllegalArgumentException("비밀번호 확인을 정확히 기입해주세요.");
        }

        validatePassword(userIdResignRequestDto.getPassword(), user.getPassword());

        user.updateUserStatus();
        userRepository.save(user);
        return new UserIdResignResponseDto("회원탈퇴 성공", user.getEmail());
    }

    //세션에서 로그인한 사용자의 정보 가져오기
    public User getSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails.getUser();
    }

    private void validatePassword(String rawPassword, String encodedPassword) throws IllegalArgumentException {
        if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
    }
}
