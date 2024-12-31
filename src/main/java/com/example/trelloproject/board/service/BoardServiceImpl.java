package com.example.trelloproject.board.service;

import com.example.trelloproject.S3.Image;
import com.example.trelloproject.S3.S3Service;
import com.example.trelloproject.board.dto.BoardCreateRequestDto;
import com.example.trelloproject.board.dto.BoardCreateResponseDto;
import com.example.trelloproject.board.dto.BoardFindResponseDto;
import com.example.trelloproject.board.entity.Board;
import com.example.trelloproject.board.repository.BoardRepository;
import com.example.trelloproject.workspace.entity.Workspace;
import com.example.trelloproject.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final S3Service s3Service;

    @Override
    public BoardCreateResponseDto createBoard(Long workspaceId, BoardCreateRequestDto boardCreateRequestDto) {

        Workspace foundWorkspace = findByWorkspaceId(workspaceId);

        Board board = new Board(boardCreateRequestDto.getTitle(), foundWorkspace);

        // 이미지 처리 함수 호출
        createImage(board, boardCreateRequestDto);

        Board savedBoard = boardRepository.save(board);

        return BoardCreateResponseDto.toDto(savedBoard);
    }

    @Override
    public List<BoardCreateResponseDto> findAllBoard(Long workspaceId) {

        List<Board> boardList = boardRepository.findAll();

        return boardList.stream().map(BoardCreateResponseDto::toDto).toList();
    }

    @Override
    public BoardFindResponseDto findBoardById(Long workspaceId, Long boardId) {

        Workspace foundWorkspace = findByWorkspaceId(workspaceId);
        Board foundBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NO_CONTENT, "해당하는 보드가 존재하지 않습니다."));

        return BoardFindResponseDto.toDto(foundBoard, foundWorkspace);
    }

    @Override
    public BoardCreateResponseDto updateBoard(Long workspaceId, Long boardId, BoardCreateRequestDto boardCreateRequestDto) {

        Board foundBoard = findByBoardId(boardId);

        // 기존 이미지 삭제 (필요한 경우)
        if (foundBoard.getImages() != null && !foundBoard.getImages().isEmpty()) {
            for (Image image : foundBoard.getImages()) {
                s3Service.delete(image.getFilePath());
            }

            foundBoard.clearImages();
        }

        // 새 이미지 처리 함수 호출
        createImage(foundBoard, boardCreateRequestDto);

        // Board 업데이트
        foundBoard.updateBoard(boardCreateRequestDto);
        Board savedBoard = boardRepository.save(foundBoard);

        return BoardCreateResponseDto.toDto(savedBoard);
    }

    @Override
    public void deleteBoard(Long workspaceId, Long boardId) {

        Board foundBoard = findByBoardId(boardId);
        boardRepository.delete(foundBoard);
    }

    // 이미지 처리 메서드
    public void createImage(Board board, BoardCreateRequestDto boardCreateRequestDto) {

        MultipartFile image = boardCreateRequestDto.getImage();
        if (image != null) {
            try {
                String filePath = s3Service.upload(image);
                String fileName = image.getOriginalFilename();

                Image imageEntity = Image.of(filePath, fileName);
                board.addImage(imageEntity);
            } catch (Exception e) {
                throw new RuntimeException("이미지 업로드 중 오류 발생: " + e.getMessage(), e);
            }
        }
    }

    public Workspace findByWorkspaceId(Long id) {

        return workspaceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NO_CONTENT, "해당하는 워크스페이스가 존재하지 않습니다."));
    }

    public Board findByBoardId(Long id) {

        return boardRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NO_CONTENT, "해당하는 보드가 존재하지 않습니다."));
    }
}
