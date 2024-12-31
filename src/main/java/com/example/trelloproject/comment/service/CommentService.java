package com.example.trelloproject.comment.service;

import com.example.trelloproject.comment.dto.CardCommentRequestDto;
import com.example.trelloproject.comment.dto.CardCommentResponseDto;
import org.springframework.security.core.Authentication;

public interface CommentService {
    CardCommentResponseDto createComment(Authentication authentication, Long cardId, CardCommentRequestDto requestDto);

    CardCommentResponseDto updateComment(Authentication authentication, Long cardId, Long commentsId, CardCommentRequestDto requestDto);

    void deleteComment(Authentication authentication, Long cardId, Long commentId);
}
