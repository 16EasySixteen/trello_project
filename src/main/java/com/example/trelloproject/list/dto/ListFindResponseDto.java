package com.example.trelloproject.list.dto;

import com.example.trelloproject.card.dto.cardFind.CardFindForListResponseDto;
import com.example.trelloproject.list.entity.BoardList;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ListFindResponseDto {

    private final Long listId;
    private final Long boardId;
    private final String title;
    private final Integer position;
    private final List<CardFindForListResponseDto> cards;
    private final LocalDateTime createdAt;
    private final LocalDateTime updateAt;

    public ListFindResponseDto(Long listId, Long boardId, String title, Integer position, List<CardFindForListResponseDto> cards, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.listId = listId;
        this.boardId = boardId;
        this.title = title;
        this.position = position;
        this.cards = cards;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public static ListFindResponseDto toDto (BoardList boardList) {
        return new ListFindResponseDto(
                boardList.getId(),
                boardList.getBoard().getId(),
                boardList.getTitle(),
                boardList.getPosition(),
                boardList.getCards().stream()
                        .map(CardFindForListResponseDto::toDto)
                        .collect(Collectors.toList()),
                boardList.getCreatedAt(),
                boardList.getUpdatedAt()
        );
    }
}
