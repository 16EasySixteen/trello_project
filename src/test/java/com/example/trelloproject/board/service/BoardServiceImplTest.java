package com.example.trelloproject.board.service;

import com.example.trelloproject.S3.Image;
import com.example.trelloproject.board.dto.BoardCreateResponseDto;
import com.example.trelloproject.board.entity.Board;
import com.example.trelloproject.board.repository.BoardRepository;
import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.workspace.entity.Workspace;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardServiceImpl boardServiceImpl;

    @Mock
    private BoardRepository boardRepository;

    @Test
    void testFindAllBoard() {

        // Given
        Long workspaceId = 1L;

        User user = new User();
        Workspace mockWorkspace = new Workspace("워크스페이스 이름", "설명", user);
        Image mockImage = new Image("https://s3.amazonaws.com/mybucket/image.png", "image.png");
        Board mockBoard = new Board("Test Board", mockWorkspace);
        mockBoard.addImage(mockImage);
        List<Board> mockBoardList = List.of(mockBoard);
        when(boardRepository.findAll()).thenReturn(mockBoardList);

        // When
        List<BoardCreateResponseDto> result = boardServiceImpl.findAllBoard(workspaceId);

        // Then
        assertThat(result).hasSize(1);
        BoardCreateResponseDto responseDto = result.get(0);
        assertThat(responseDto.getBoardId()).isEqualTo(mockBoard.getId());
        assertThat(responseDto.getWorkspaceId()).isEqualTo(mockWorkspace.getWorkspaceId());
        assertThat(responseDto.getTitle()).isEqualTo(mockBoard.getTitle());
        assertThat(responseDto.getImage().getFilePath()).isEqualTo(mockImage.getFilePath());
        assertThat(responseDto.getImage().getFileName()).isEqualTo(mockImage.getFileName());
    }
}