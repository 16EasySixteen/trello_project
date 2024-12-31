package com.example.trelloproject.emoji.dto;

import lombok.Getter;

@Getter
public class EmojiRequestDto {
    private String symbol;

    public EmojiRequestDto(String symbol) {
        this.symbol = symbol;
    }

    public EmojiRequestDto() {
    }
}
