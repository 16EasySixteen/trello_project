package com.example.trelloproject.card.dto.cardUpdate;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardUpdateResponseDto {

    private Long id;
    private String newTitle;
    private String newDescription;
    private LocalDateTime newEndAt;
    private final LocalDateTime updateAt;

    public CardUpdateResponseDto(Long id, String newTitle, String newDescription, LocalDateTime newEndAt, LocalDateTime updateAt) {
        this.id = id;
        this.newTitle = newTitle;
        this.newDescription = newDescription;
        this.newEndAt = newEndAt;
        this.updateAt = updateAt;
    }
}
