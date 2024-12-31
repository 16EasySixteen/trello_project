package com.example.trelloproject.list.controller;

import com.example.trelloproject.list.dto.ListCreateRequestDto;
import com.example.trelloproject.list.dto.ListCreateResponseDto;
import com.example.trelloproject.list.dto.ListUpdateRequestDto;
import com.example.trelloproject.list.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workspaces/{workspaceId}/boards/{boardId}/lists")
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    // 리스트 생성
    @PostMapping
    public ResponseEntity<ListCreateResponseDto> createList(@PathVariable Long workspaceId, @PathVariable Long boardId, @RequestBody ListCreateRequestDto listCreateRequestDto) {

        ListCreateResponseDto createdList = listService.creatList(workspaceId, boardId, listCreateRequestDto);

        return new ResponseEntity<>(createdList, HttpStatus.CREATED);
    }

    // 리스트 수정
    @PatchMapping("/{listId}")
    public ResponseEntity<ListCreateResponseDto> updateList(@PathVariable Long workspaceId, @PathVariable Long boardId, @PathVariable Long listId, @RequestBody ListUpdateRequestDto listUpdateRequestDto) {

        ListCreateResponseDto updatedList = listService.updateList(workspaceId, boardId, listId, listUpdateRequestDto);

        return new ResponseEntity<>(updatedList, HttpStatus.OK);
    }

    // 리스트 삭제
    @DeleteMapping("/{listId}")
    public ResponseEntity<String> deleteList(@PathVariable Long workspaceId, @PathVariable Long boardId, @PathVariable Long listId) {

        listService.deleteList(workspaceId, boardId, listId);

        return new ResponseEntity<>("정상적으로 삭제되었습니다.", HttpStatus.OK);
    }

}
