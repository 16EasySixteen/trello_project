package com.example.trelloproject.workspace.dto;

import com.example.trelloproject.user.enumclass.MemberRole;
import lombok.Getter;

@Getter
public class WorkspaceInviteRequestDto {
    private String email;
    private MemberRole memberRole;

    public WorkspaceInviteRequestDto(String email, MemberRole memberRole) {
        this.email = email;
        this.memberRole = memberRole;
    }
}
