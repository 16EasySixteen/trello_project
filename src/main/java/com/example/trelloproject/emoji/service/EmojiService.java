package com.example.trelloproject.emoji.service;

import com.example.trelloproject.emoji.dto.EmojiRequestDto;
import com.example.trelloproject.emoji.dto.EmojiResponseDto;
import org.springframework.security.core.Authentication;

public interface EmojiService {
    EmojiResponseDto createEmoji(Authentication authentication, Long commentId, EmojiRequestDto requestDto);

    void deleteEmoji(Long emojiId, Authentication authentication);
}
