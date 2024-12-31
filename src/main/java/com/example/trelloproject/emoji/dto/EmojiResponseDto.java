package com.example.trelloproject.emoji.dto;

import lombok.Getter;

@Getter
public class EmojiResponseDto {
    private Long id;
    private String emoji;

    public EmojiResponseDto(Long id, String emoji) {
        this.id = id;
        this.emoji = emoji;
    }
}
