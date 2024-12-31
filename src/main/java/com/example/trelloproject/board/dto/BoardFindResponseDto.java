package com.example.trelloproject.board.dto;

import com.example.trelloproject.S3.ImageResponseDto;
import com.example.trelloproject.board.entity.Board;
import com.example.trelloproject.list.dto.ListFindResponseDto;
import com.example.trelloproject.workspace.entity.Workspace;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardFindResponseDto {

    private final Long boardId;
    private final Long workspaceId;
    private final String title;
    private final List<ImageResponseDto> images;
    private final List<ListFindResponseDto> lists;
    private final LocalDateTime createdAt;
    private final LocalDateTime updateAt;

    public BoardFindResponseDto(Long boardId, Long workspaceId, String title, List<ImageResponseDto> images, List<ListFindResponseDto> lists, LocalDateTime createdAt, LocalDateTime updateAt) {

        this.boardId = boardId;
        this.workspaceId = workspaceId;
        this.title = title;
        this.images = images;
        this.lists = lists;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public static BoardFindResponseDto toDto(Board board, Workspace workspace) {

        return new BoardFindResponseDto(
                board.getId(),
                workspace.getWorkspaceId(),
                board.getTitle(),
                board.getImages().stream()
                        .map(ImageResponseDto::toDto)
                        .collect(Collectors.toList()),
                board.getBoardLists().stream()
                        .map(ListFindResponseDto::toDto)
                        .collect(Collectors.toList()),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
