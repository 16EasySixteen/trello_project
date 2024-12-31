package com.example.trelloproject.user.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthenticationScheme {
    BEARER("Bearer");

    private final String name;

    // Authorization의 헤더 값에 적용할 prefix를 생성
    public static String generateType(AuthenticationScheme scheme) {
        return scheme.getName() + " ";
    }
}
