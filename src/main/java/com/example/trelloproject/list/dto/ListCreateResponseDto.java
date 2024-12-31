package com.example.trelloproject.list.dto;

import com.example.trelloproject.list.entity.BoardList;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ListCreateResponseDto {

    private final Long listId;
    private final Long boardId;
    private final String title;
    private final Integer position;
    private final LocalDateTime createdAt;
    private final LocalDateTime updateAt;

    public ListCreateResponseDto(Long listId, Long boardId, String title, Integer position, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.listId = listId;
        this.boardId = boardId;
        this.title = title;
        this.position = position;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public static ListCreateResponseDto toDto (BoardList boardList) {
        return new ListCreateResponseDto(
                boardList.getId(),
                boardList.getBoard().getId(),
                boardList.getTitle(),
                boardList.getPosition(),
                boardList.getCreatedAt(),
                boardList.getUpdatedAt()
        );
    }
}
