package com.example.trelloproject.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardCommentResponseDto {
    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public CardCommentResponseDto(Long id, Long userId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
