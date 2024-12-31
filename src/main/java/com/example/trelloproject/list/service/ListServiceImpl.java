package com.example.trelloproject.list.service;

import com.example.trelloproject.board.entity.Board;
import com.example.trelloproject.board.repository.BoardRepository;
import com.example.trelloproject.list.dto.ListCreateRequestDto;
import com.example.trelloproject.list.dto.ListCreateResponseDto;
import com.example.trelloproject.list.dto.ListUpdateRequestDto;
import com.example.trelloproject.list.entity.BoardList;
import com.example.trelloproject.list.repository.ListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListServiceImpl implements ListService{

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;

    @Override
    public ListCreateResponseDto creatList(Long workspaceId, Long boardId, ListCreateRequestDto listCreateRequestDto) {

        Board foundBoard = findByBoardId(boardId);

        // 보드아이디로 속한 리스트 갯수 세기
        Integer listCount = listRepository.countByBoardId(boardId);
        BoardList boardList = new BoardList(listCreateRequestDto.getTitle(), listCount + 1, foundBoard);
        BoardList savedBoardList = listRepository.save(boardList);

        return ListCreateResponseDto.toDto(savedBoardList);
    }

    @Override
    public ListCreateResponseDto updateList(Long workspaceId, Long boardId, Long listId, ListUpdateRequestDto listUpdateRequestDto) {

        Board foundBoard = findByBoardId(boardId);

        // 순서를 바꿀 리스트
        BoardList foundBoardList = findByListId(listId);
        // 보드아이디에 해당하는 리스트들의 집합
        List<BoardList> boardLists = listRepository.findByBoardId(boardId);

        // 기존 포지션 < 바뀔 포지션 : position--
        if (foundBoardList.getPosition() < listUpdateRequestDto.getPosition()) {

            // 기존 포지션 초과 ~ 바뀔 포지션 이하
            for (BoardList target : boardLists) {
                if (foundBoardList.getPosition() < target.getPosition() && target.getPosition() <= listUpdateRequestDto.getPosition()) {
                    target.downPosition();
                }
            }
        } else {    // 기존 포지션 > 바뀔 포지션 : position++

            // 바뀔 포지션 이상 ~ 기존 포지션 미만
            for (BoardList target : boardLists) {
                if (listUpdateRequestDto.getPosition() <= target.getPosition() && target.getPosition() < foundBoardList.getPosition()) {
                    target.upPosition();
                }
            }
        }

        // 제목과 기존포지션->바뀔포지션 변경
        foundBoardList.updatePosition(listUpdateRequestDto.getTitle(), listUpdateRequestDto.getPosition());

        listRepository.save(foundBoardList);
        return ListCreateResponseDto.toDto(foundBoardList);
    }

    @Override
    public void deleteList(Long workspaceId, Long boardId, Long listId) {

        BoardList foundBoardList = findByListId(listId);
        listRepository.delete(foundBoardList);
    }

    public Board findByBoardId(Long Id) {

        return boardRepository.findById(Id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NO_CONTENT, "해당하는 보드가 존재하지 않습니다."));

    }

    public BoardList findByListId(Long Id) {

        return listRepository.findById(Id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NO_CONTENT, "해당하는 리스트가 존재하지 않습니다."));

    }

}
