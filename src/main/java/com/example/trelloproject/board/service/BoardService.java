package com.example.trelloproject.board.service;

import com.example.trelloproject.board.dto.BoardCreateRequestDto;
import com.example.trelloproject.board.dto.BoardCreateResponseDto;
import com.example.trelloproject.board.dto.BoardFindResponseDto;

import java.util.List;

public interface BoardService {

    BoardCreateResponseDto createBoard(Long workspaceId, BoardCreateRequestDto boardCreateRequestDto);

    List<BoardCreateResponseDto> findAllBoard(Long workspaceId);

    BoardFindResponseDto findBoardById(Long workspaceId, Long boardId);

    BoardCreateResponseDto updateBoard(Long workspaceId, Long boardId, BoardCreateRequestDto boardCreateRequestDto);

    void deleteBoard(Long workspaceId, Long boardId);
}
