package com.example.trelloproject.card.dto.cardCreate;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class CardCreateResponseDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime endAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updateAt;

    public CardCreateResponseDto(Long id, String title, String description, LocalDateTime endAt, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

}
