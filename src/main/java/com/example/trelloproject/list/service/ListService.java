package com.example.trelloproject.list.service;

import com.example.trelloproject.list.dto.ListCreateRequestDto;
import com.example.trelloproject.list.dto.ListCreateResponseDto;
import com.example.trelloproject.list.dto.ListUpdateRequestDto;

public interface ListService {

   ListCreateResponseDto creatList(Long workspaceId, Long boardId, ListCreateRequestDto listCreateRequestDto);

   ListCreateResponseDto updateList(Long workspaceId, Long boardId, Long listId, ListUpdateRequestDto listUpdateRequestDto);

   void deleteList(Long workspaceId, Long boardId, Long listId);
}
