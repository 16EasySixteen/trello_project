package com.example.trelloproject.card.dto.cardFind;

import com.example.trelloproject.card.entity.Card;
import lombok.Getter;

@Getter
public class CardFindForListResponseDto {

    private Long id;
    private String title;
    private String description;

    public CardFindForListResponseDto(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public static CardFindForListResponseDto toDto(Card card) {

        return new CardFindForListResponseDto(
                card.getId(),
                card.getTitle(),
                card.getDescription()
        );
    }
}