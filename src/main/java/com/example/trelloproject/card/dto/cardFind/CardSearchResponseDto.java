package com.example.trelloproject.card.dto.cardFind;

import lombok.Getter;

@Getter
public class CardSearchResponseDto {

    private Long id;
    private String title;

    public CardSearchResponseDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
