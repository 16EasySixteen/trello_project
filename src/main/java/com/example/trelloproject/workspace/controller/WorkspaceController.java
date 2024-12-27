package com.example.trelloproject.workspace.controller;

import com.example.trelloproject.workspace.dto.*;
import com.example.trelloproject.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    /**
     * 워크스페이스 생성 API
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(
            @RequestBody WorkspaceRequestDto requestDto,
            Authentication authentication
    ){
        WorkspaceResponseDto responseDto = workspaceService.createWorkspace(authentication,requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 워크스페이스 조회 API
     * @return
     */
    @GetMapping
    public ResponseEntity<List<WorkspaceFindResponseDto>> getAllWorkspace() {
        List<WorkspaceFindResponseDto> responseDtos = workspaceService.getAllWorkspace();
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    /**
     * 워크스페이스 업데이트 API
     * @param id
     * @param requestDto
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(
            @PathVariable Long id,
            @RequestBody WorkspaceRequestDto requestDto
    ){
        WorkspaceResponseDto responseDto = workspaceService.updateWorkspace(id, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     *  워크스페이스 삭제 API
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkspace(
            @PathVariable Long id
    ){
        workspaceService.deleteWorkspace(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    /**
     * 워크스페이스 초대 및 권한 설정 API
     * @param workspaceId
     * @param requestDto
     * @param authentication
     * @return
     */
    @PostMapping("/{workspaceId}/invitation")
    public ResponseEntity<WorkspaceInviteResponseDto> inviteWorkspace(
            @PathVariable Long workspaceId,
            @RequestBody WorkspaceInviteRequestDto requestDto,
            Authentication authentication
    ){
        WorkspaceInviteResponseDto responseDto = workspaceService.inviteWorkspace(authentication,workspaceId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
