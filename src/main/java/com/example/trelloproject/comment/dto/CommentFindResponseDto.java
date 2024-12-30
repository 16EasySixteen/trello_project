package com.example.trelloproject.comment.dto;

import com.example.trelloproject.comment.entity.Comment;
import com.example.trelloproject.emoji.dto.EmojiFindResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CommentFindResponseDto {
    private final Long commentId;
    private final Long userId;
    private final String content;
    private final List<EmojiFindResponseDto> Emojis;

    public CommentFindResponseDto(Comment comment, List<EmojiFindResponseDto> emoji) {
        this.commentId = comment.getCommentId();
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.Emojis = emoji;
    }
}
