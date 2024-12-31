package com.example.trelloproject.user.enumclass;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
public enum UserRole {
    USER("USER"), ADMIN("ADMIN");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    public static UserRole of(String name) throws IllegalArgumentException {
        for (UserRole role : values()) {
            if (role.getName().equals(name)) {
                return role;
            }
        }

        throw new IllegalArgumentException("해당하는 권한이 존재하지 않습니다." + name);
    }

    public List<SimpleGrantedAuthority> getAuthority() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + name));
    }
}
