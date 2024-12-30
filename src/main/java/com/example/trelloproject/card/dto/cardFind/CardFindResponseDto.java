package com.example.trelloproject.card.dto.cardFind;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardFindResponseDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime endAt;


    public CardFindResponseDto(Long id, String title, String description, LocalDateTime endAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.endAt = endAt;

    }
}
