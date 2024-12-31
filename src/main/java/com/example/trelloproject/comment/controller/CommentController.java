package com.example.trelloproject.comment.controller;

import com.example.trelloproject.comment.dto.CardCommentRequestDto;
import com.example.trelloproject.comment.dto.CardCommentResponseDto;
import com.example.trelloproject.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspaces/{workspaceId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/cards/{cardId}/comments")
    public ResponseEntity<CardCommentResponseDto> createComment(
            @PathVariable Long cardId,
            @RequestBody CardCommentRequestDto requestDto,
            Authentication authentication
    ){
        CardCommentResponseDto responseDto = commentService.createComment(authentication, cardId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<CardCommentResponseDto> updateComment(
            @PathVariable Long cardId,
            @PathVariable Long commentId,
            @RequestBody CardCommentRequestDto requestDto,
            Authentication authentication
    ){
        CardCommentResponseDto responseDto = commentService.updateComment(authentication, cardId, commentId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long cardId,
            @PathVariable Long commentId,
            Authentication authentication
    ){
        commentService.deleteComment(authentication,cardId, commentId);
        return new ResponseEntity<>("Comment deleted", HttpStatus.OK);
    }

}
