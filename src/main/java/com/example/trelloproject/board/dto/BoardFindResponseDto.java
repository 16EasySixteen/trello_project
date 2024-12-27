package com.example.trelloproject.board.dto;

import com.example.trelloproject.board.entity.Board;
import com.example.trelloproject.list.dto.ListCreateResponseDto;
import com.example.trelloproject.workspace.entity.Workspace;
import lombok.Getter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardFindResponseDto {

    private final Long boardId;
    private final Long workspaceId;
    private final String title;
    private final File image;
    private final List<ListCreateResponseDto> lists;
    private final LocalDateTime createdAt;
    private final LocalDateTime updateAt;

    public BoardFindResponseDto(Long boardId, Long workspaceId, String title, File image, List<ListCreateResponseDto> lists, LocalDateTime createdAt, LocalDateTime updateAt) {

        this.boardId = boardId;
        this.workspaceId = workspaceId;
        this.title = title;
        this.image = image;
        this.lists = lists;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public static BoardFindResponseDto toDto(Board board, Workspace workspace) {

        return new BoardFindResponseDto(
                board.getId(),
                workspace.getWorkspaceId(),
                board.getTitle(),
                board.getImage(),
                board.getBoardLists().stream()
                        .map(ListCreateResponseDto::toDto)
                        .collect(Collectors.toList()),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}