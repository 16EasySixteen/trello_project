package com.example.trelloproject.card.dto.cardFind;

import com.example.trelloproject.card.entity.AddFile;
import com.example.trelloproject.card.entity.Manager;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardFindOneResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime endAt;

    private List commentList;

    public CardFindOneResponseDto(Long id, String title, String description, LocalDateTime endAt, List commentList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.endAt = endAt;
        this.commentList = commentList;
    }

    public CardFindOneResponseDto(Long id, String title){
        this.id = id;
        this.title = title;
    }
}
