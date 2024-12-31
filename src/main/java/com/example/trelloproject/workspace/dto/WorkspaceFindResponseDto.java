package com.example.trelloproject.workspace.dto;

import com.example.trelloproject.workspace.entity.Workspace;
import lombok.Getter;

@Getter
public class WorkspaceFindResponseDto {
    private final Long id;
    private final String name;

    public WorkspaceFindResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public WorkspaceFindResponseDto(Workspace workspace) {
        this.id = workspace.getWorkspaceId();
        this.name = workspace.getName();
    }
}
