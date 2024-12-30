package com.example.trelloproject.card.dto.addFile;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AddFileResponseDto {

    private Long id;
    private String filePath;
    private String fileName;
    private String fileType;
    private final LocalDateTime createdAt;
    private final LocalDateTime updateAt;

    public AddFileResponseDto(Long id, String filePath, String fileName, String fileType, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }
}
