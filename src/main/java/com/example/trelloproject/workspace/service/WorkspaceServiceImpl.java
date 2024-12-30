package com.example.trelloproject.workspace.service;

import com.example.trelloproject.user.config.auth.UserDetailsImpl;
import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.entity.UserWorkspace;
import com.example.trelloproject.user.repository.UserRepository;
import com.example.trelloproject.workspace.dto.*;
import com.example.trelloproject.user.enumclass.MemberRole;
import com.example.trelloproject.workspace.dto.WorkspaceRequestDto;
import com.example.trelloproject.workspace.dto.WorkspaceFindResponseDto;
import com.example.trelloproject.workspace.dto.WorkspaceResponseDto;
import com.example.trelloproject.workspace.entity.Workspace;
import com.example.trelloproject.workspace.repository.UserWorkspaceRepository;
import com.example.trelloproject.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    // 추가 사항
    private final UserWorkspaceService userWorkspaceService;

    /**
     * 워크스페이스 생성
     * @param authentication
     * @param requestDto
     * @return
     */
    @Transactional
    @Override
    public WorkspaceResponseDto createWorkspace(Authentication authentication, WorkspaceRequestDto requestDto) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User findUser = userDetails.getUser();

        Workspace workspace = new Workspace(requestDto.getName(), requestDto.getDescription(),findUser);
        Workspace newWorkspace = workspaceRepository.save(workspace);

        // 추가 사항
        userWorkspaceService.createUserWorkspace(findUser.getId(), workspace.getWorkspaceId(), "ADMIN", MemberRole.WSADMIN);

        return new WorkspaceResponseDto(
                newWorkspace.getWorkspaceId(),
                newWorkspace.getUser().getId(),
                newWorkspace.getName(),
                newWorkspace.getDescription(),
                newWorkspace.getCreatedAt(),
                newWorkspace.getUpdatedAt()
        );
    }

    /**
     * 워크스페이스 모든 조회
     * @return 워크스페이스 리스트
     */
    @Override
    public List<WorkspaceFindResponseDto> getAllWorkspace(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User findUser = userDetails.getUser();

        List<Workspace> workspaces = workspaceRepository.findByUserId(findUser.getId());
        if(workspaces.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"워크 스페이스를 찾을 수 없습니다.");
        }

        return workspaces.stream()
                .map(WorkspaceFindResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 워크스페이스 업데이트
     * @param id
     * @param requestDto
     * @return
     */
    @Transactional
    @Override
    public WorkspaceResponseDto updateWorkspace(Long id, WorkspaceRequestDto requestDto) {

        Workspace findWorkspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        findWorkspace.updateWorkspace(requestDto.getName(), requestDto.getDescription());
        Workspace updateWorkspace = workspaceRepository.save(findWorkspace);
        return new WorkspaceResponseDto(
                updateWorkspace.getWorkspaceId(),
                updateWorkspace.getUser().getId(),
                updateWorkspace.getName(),
                updateWorkspace.getDescription(),
                updateWorkspace.getCreatedAt(),
                updateWorkspace.getUpdatedAt());
    }

    /**
     * 워크스페이스 삭제
     * @param id
     */
    @Transactional
    @Override
    public void deleteWorkspace(Long id) {

        workspaceRepository.deleteById(id);
    }

    /**
     * 워크스페이스 초대 및 권한 설정
     * @param authentication
     * @param workspaceId
     * @param requestDto
     * @return
     */
    @Transactional
    @Override
    public WorkspaceInviteResponseDto inviteWorkspace(Authentication authentication, Long workspaceId, WorkspaceInviteRequestDto requestDto) {
        // 로그인한 유저
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User loginUser = userDetails.getUser();

        // 워크 스페이스 찾음
        Workspace findWorkspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"워크스페이스를 찾을 수 없습니다."));

        // 초대할 유저 조회
        User findUser = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다."));

        // 본인한테는 초대할 수 없음
        if(loginUser.getId().equals(findUser.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 요청입니다.");
        }


        UserWorkspace newWorkspace = new UserWorkspace("INVITE", requestDto.getMemberRole(),findUser,findWorkspace);
        UserWorkspace newUserWorkspace = userWorkspaceRepository.save(newWorkspace);

        return new WorkspaceInviteResponseDto(
                newUserWorkspace.getId(),
                newUserWorkspace.getUser().getId(),
                newUserWorkspace.getUser().getEmail(),
                newUserWorkspace.getInvitationStatus(),
                newUserWorkspace.getMemberRole()
        );
    }

    /**
     * 워크스페이스 초대 응답
     * @param workspaceId
     * @param id
     * @param authentication
     * @param requestDto
     * @return
     */
    @Transactional
    @Override
    public WorkspaceInviteResponseDto inviteAcceptWorkspace(Long workspaceId, Long id, Authentication authentication, WorkspaceInviteAcceptRequestDto requestDto) {
        // Workspace 조회
        Workspace findWorkspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"해당 워크스페이스가 없습니다."));
        // UserWorkspace 조회
        UserWorkspace findUserWorkspace = userWorkspaceRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"초대된 워크스페이스가 없습니다."));
        // 유저 조회
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User findUser = userDetails.getUser();

        // userWorkspace에 있는 유저와 로그인한 유저가 다를 경우
        if(!findUser.getId().equals(findUserWorkspace.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        findUserWorkspace.updateStatus(requestDto.getStatus());
        UserWorkspace newUserWorkspace = userWorkspaceRepository.save(findUserWorkspace);

        return new WorkspaceInviteResponseDto(
                newUserWorkspace.getId(),
                newUserWorkspace.getUser().getId(),
                newUserWorkspace.getUser().getEmail(),
                newUserWorkspace.getInvitationStatus(),
                newUserWorkspace.getMemberRole()
        );

    }


}
