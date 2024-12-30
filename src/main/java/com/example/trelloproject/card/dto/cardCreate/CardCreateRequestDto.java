package com.example.trelloproject.card.dto.cardCreate;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class CardCreateRequestDto {

    private String title;
    private String description;
    private LocalDateTime endAt;


    public CardCreateRequestDto(String title, String description, LocalDateTime endAt) {
        this.description = description;
        this.title = title;
        this.endAt = endAt;
    }
}
