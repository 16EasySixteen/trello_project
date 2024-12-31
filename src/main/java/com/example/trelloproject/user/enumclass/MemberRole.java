package com.example.trelloproject.user.enumclass;

import lombok.Getter;

@Getter
public enum MemberRole {
    WSADMIN("WSADMIN"), MEMBER("MEMBER"), ONLYREAD("ONLYREAD");

    private String name;

    MemberRole(String name) {
        this.name = name;
    }

    public static MemberRole of(String name) throws IllegalArgumentException {
        for (MemberRole role : values()) {
            if (role.getName().equals(name)) {
                return role;
            }
        }

        throw new IllegalArgumentException("해당하는 권한이 존재하지 않습니다." + name);
    }
}
