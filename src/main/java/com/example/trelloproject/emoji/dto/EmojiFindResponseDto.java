package com.example.trelloproject.emoji.dto;

import com.example.trelloproject.emoji.entity.Emoji;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmojiFindResponseDto {
    private final Long emojiId;
    private final String symbol;

    public EmojiFindResponseDto(Emoji emoji) {
        this.emojiId = emoji.getEmojiId();
        this.symbol = emoji.getSymbol();
    }
}
