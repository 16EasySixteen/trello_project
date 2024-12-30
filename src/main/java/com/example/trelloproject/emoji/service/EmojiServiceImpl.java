package com.example.trelloproject.emoji.service;

import com.example.trelloproject.comment.entity.Comment;
import com.example.trelloproject.comment.repository.CommentRepository;
import com.example.trelloproject.emoji.dto.EmojiRequestDto;
import com.example.trelloproject.emoji.dto.EmojiResponseDto;
import com.example.trelloproject.emoji.entity.Emoji;
import com.example.trelloproject.emoji.repository.EmojiRepository;
import com.example.trelloproject.user.config.auth.UserDetailsImpl;
import com.example.trelloproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EmojiServiceImpl implements EmojiService {

    private final CommentRepository commentRepository;
    private final EmojiRepository emojiRepository;

    @Override
    public EmojiResponseDto createEmoji(Authentication authentication, Long commentId, EmojiRequestDto requestDto) {
        // 댓글 조회
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "comment doesn't exist"));

        // 로그인한 유저 조회
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        User findUser = user.getUser();

        Emoji newEmoji = new Emoji(requestDto.getSymbol(), findUser,findComment);
        Emoji createEmoji = emojiRepository.save(newEmoji);

        return new EmojiResponseDto(
                createEmoji.getEmojiId(),
                createEmoji.getSymbol()
        );
    }

    @Override
    public void deleteEmoji(Long emojiId, Authentication authentication) {
        Emoji findEmoji = emojiRepository.findById(emojiId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "emoji doesn't exist"));

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        User findUser = user.getUser();

        if(!findEmoji.getUser().getId().equals(findUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        emojiRepository.deleteById(emojiId);
    }
}
