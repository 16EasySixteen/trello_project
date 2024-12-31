package com.example.trelloproject.board.dto;

import com.example.trelloproject.S3.ImageResponseDto;
import com.example.trelloproject.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardCreateResponseDto {

    private final Long boardId;
    private final Long workspaceId;
    private final String title;
    private final ImageResponseDto image;
    private final LocalDateTime createdAt;
    private final LocalDateTime updateAt;

    public BoardCreateResponseDto(Long boardId, Long workspaceId, String title, ImageResponseDto image, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.boardId = boardId;
        this.workspaceId = workspaceId;
        this.title = title;
        this.image = image;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public static BoardCreateResponseDto toDto(Board savedBoard) {
        return new BoardCreateResponseDto(
                savedBoard.getId(),
                savedBoard.getWorkspace().getWorkspaceId(),
                savedBoard.getTitle(),
                savedBoard.getImages().stream()
                        .findFirst()
                        .map(ImageResponseDto::toDto)
                        .orElse(new ImageResponseDto()),
                savedBoard.getCreatedAt(),
                savedBoard.getUpdatedAt()
        );
    }
}
