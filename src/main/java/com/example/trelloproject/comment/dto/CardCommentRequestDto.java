package com.example.trelloproject.comment.dto;

import lombok.Getter;

@Getter
public class CardCommentRequestDto {
    public String content;

    public CardCommentRequestDto(String content) {
        this.content = content;
    }

    public CardCommentRequestDto() {
    }
}
