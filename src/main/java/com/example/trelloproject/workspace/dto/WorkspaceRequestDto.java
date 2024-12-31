package com.example.trelloproject.workspace.dto;

import lombok.Getter;

@Getter
public class WorkspaceRequestDto {
    private final String name;
    private final String description;

    public WorkspaceRequestDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
