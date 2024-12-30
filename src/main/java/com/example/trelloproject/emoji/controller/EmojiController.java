package com.example.trelloproject.emoji.controller;

import com.example.trelloproject.emoji.dto.EmojiRequestDto;
import com.example.trelloproject.emoji.dto.EmojiResponseDto;
import com.example.trelloproject.emoji.service.EmojiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/workspaces/{workspaceId}")
@RequiredArgsConstructor
public class EmojiController {

    private final EmojiService emojiService;

    @PostMapping("/comments/{commentId}/emojis")
    public ResponseEntity<EmojiResponseDto> createEmoji(
            @PathVariable Long commentId,
            @RequestBody EmojiRequestDto requestDto,
            Authentication authentication
    ){
        EmojiResponseDto responseDto = emojiService.createEmoji(authentication, commentId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/emojis/{emojiId}")
    public ResponseEntity<String> deleteEmoji(
            @PathVariable Long emojiId,
            Authentication authentication
    ){
        emojiService.deleteEmoji(emojiId, authentication);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

}
