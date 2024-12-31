package com.example.trelloproject.workspace.dto;

import lombok.Getter;

@Getter
public class WorkspaceInviteAcceptRequestDto {
    private String status;

    public WorkspaceInviteAcceptRequestDto() {
    }

    public WorkspaceInviteAcceptRequestDto(String status) {
        this.status = status;
    }
}
