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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void testDeleteBoard() {
        // Given
        Long workspaceId = 1L;
        Long boardId = 1L;

        Board mockBoard = new Board("Test Board", null);
        when(boardRepository.findById(boardId)).thenReturn(java.util.Optional.of(mockBoard));

        // When
        boardServiceImpl.deleteBoard(workspaceId, boardId);

        // Then
        verify(boardRepository, times(1)).delete(mockBoard);
    }

    @Test
    void testDeleteBoard_NotFound() {
        // Given
        Long workspaceId = 1L;
        Long boardId = 1L;

        // When: Board가 존재하지 않을 경우 (Optional.empty() 반환)
        when(boardRepository.findById(boardId)).thenReturn(java.util.Optional.empty());

        // Then: ResponseStatusException이 발생해야 한다는 것을 검증
        assertThrows(ResponseStatusException.class, () -> {
            boardServiceImpl.deleteBoard(workspaceId, boardId);
        });
    }
}