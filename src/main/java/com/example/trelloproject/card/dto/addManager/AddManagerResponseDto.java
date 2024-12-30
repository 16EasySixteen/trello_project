package com.example.trelloproject.card.dto.addManager;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AddManagerResponseDto {
    private Long ManagerId;
    private String ManagerName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updateAt;

    public AddManagerResponseDto(Long managerId, String managerName ,LocalDateTime createdAt, LocalDateTime updateAt) {
        this.ManagerId = managerId;
        this.ManagerName = managerName;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }
}
