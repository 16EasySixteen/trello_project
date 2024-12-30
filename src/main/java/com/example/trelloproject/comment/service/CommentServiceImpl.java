package com.example.trelloproject.comment.service;

import com.example.trelloproject.card.entity.Card;
import com.example.trelloproject.card.repository.CardRepository;
import com.example.trelloproject.card.repository.CardRepositoryImpl;
import com.example.trelloproject.comment.repository.CommentRepository;
import com.example.trelloproject.comment.dto.CardCommentRequestDto;
import com.example.trelloproject.comment.dto.CardCommentResponseDto;
import com.example.trelloproject.comment.entity.Comment;
import com.example.trelloproject.user.config.auth.UserDetailsImpl;
import com.example.trelloproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;

    @Override
    public CardCommentResponseDto createComment(Authentication authentication, Long cardId, CardCommentRequestDto requestDto) {

        Card findCard = cardRepository.findById(cardId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "card doesn't exist"));

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        User findUser = user.getUser();

        Comment newComment = new Comment(requestDto.getContent(), findUser, findCard);
        Comment createComment = commentRepository.save(newComment);

        return new CardCommentResponseDto(
                createComment.getCommentId(),
                createComment.getUser().getId(),
                createComment.getContent(),
                createComment.getCreatedAt(),
                createComment.getUpdatedAt()
        );
    }

    @Override
    public CardCommentResponseDto updateComment(Authentication authentication, Long cardId, Long commentId, CardCommentRequestDto requestDto) {
        // 댓글 조회
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment doesn't exist"));

        // 로그인한 유저 조회
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        User findUser = user.getUser();

        // 로그인한 유저가 작성한 댓글인지 확인
        if(!findUser.getId().equals(findComment.getUser().getId())){
            log.info("권한없음");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"본인이 작성한 댓글만 수정 가능");
        }

        findComment.updateComment(requestDto.getContent());
        Comment newComment = commentRepository.save(findComment);

        return new CardCommentResponseDto(
                newComment.getCommentId(),
                newComment.getUser().getId(),
                newComment.getContent(),
                newComment.getCreatedAt(),
                newComment.getUpdatedAt()
        );
    }

    @Override
    public void deleteComment(Authentication authentication, Long cardId, Long commentId) {
        // 댓글 조회
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment doesn't exist"));

        // 로그인한 유저 조회
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        User findUser = user.getUser();

        // 로그인한 유저가 작성한 댓글인지 확인
        if(!findUser.getId().equals(findComment.getUser().getId())){
            log.info("권한없음");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"본인이 작성한 댓글만 삭제 가능");
        }

        commentRepository.delete(findComment);

    }
}
