package com.example.trelloproject.board.controller;

import com.example.trelloproject.board.dto.BoardCreateRequestDto;
import com.example.trelloproject.board.dto.BoardCreateResponseDto;
import com.example.trelloproject.board.dto.BoardFindResponseDto;
import com.example.trelloproject.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @PostMapping
    public ResponseEntity<BoardCreateResponseDto> createBoard(@PathVariable Long workspaceId, @Valid @ModelAttribute BoardCreateRequestDto boardCreateRequestDto) {

        BoardCreateResponseDto createdBoard = boardService.createBoard(workspaceId, boardCreateRequestDto);

        return new ResponseEntity<>(createdBoard, HttpStatus.CREATED);
    }

    // 보드 다건 조회
    @GetMapping
    public ResponseEntity<List<BoardCreateResponseDto>> findAllBoard(@PathVariable Long workspaceId) {

        List<BoardCreateResponseDto> foundBoards = boardService.findAllBoard(workspaceId);

        return new ResponseEntity<>(foundBoards, HttpStatus.OK);
    }

    // 보드 단건 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardFindResponseDto> findBoardById(@PathVariable Long workspaceId, @PathVariable Long boardId) {

        BoardFindResponseDto foundBoard = boardService.findBoardById(workspaceId, boardId);

        return new ResponseEntity<>(foundBoard, HttpStatus.OK);
    }

    // 보드 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardCreateResponseDto> updateBoard(@PathVariable Long workspaceId, @PathVariable Long boardId, @Valid @ModelAttribute BoardCreateRequestDto boardCreateRequestDto) {

        BoardCreateResponseDto updatedBoard = boardService.updateBoard(workspaceId, boardId, boardCreateRequestDto);

        return new ResponseEntity<>(updatedBoard, HttpStatus.OK);
    }

    // 보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long workspaceId, @PathVariable Long boardId) {

        boardService.deleteBoard(workspaceId, boardId);

        return new ResponseEntity<>("정상적으로 삭제되었습니다.", HttpStatus.OK);
    }

}