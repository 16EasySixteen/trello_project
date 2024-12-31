package com.example.trelloproject.workspace.dto;

import com.example.trelloproject.user.enumclass.MemberRole;
import lombok.Getter;

import java.lang.reflect.Member;

@Getter
public class WorkspaceInviteResponseDto {
    private Long id;
    private Long userId;
    private String email;
    private String status;
    private MemberRole memberRole;

    public WorkspaceInviteResponseDto(Long id, Long userId, String email, String status, MemberRole memberRole) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.status = status;
        this.memberRole = memberRole;
    }
}
