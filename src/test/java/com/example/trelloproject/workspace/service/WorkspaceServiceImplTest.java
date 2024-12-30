package com.example.trelloproject.workspace.service;

import com.example.trelloproject.user.config.auth.UserDetailsImpl;
import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.entity.UserWorkspace;
import com.example.trelloproject.user.enumclass.MemberRole;
import com.example.trelloproject.user.enumclass.UserRole;
import com.example.trelloproject.user.repository.UserRepository;
import com.example.trelloproject.workspace.dto.*;
import com.example.trelloproject.workspace.entity.Workspace;
import com.example.trelloproject.workspace.repository.UserWorkspaceRepository;
import com.example.trelloproject.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceImplTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserWorkspaceRepository userWorkspaceRepository;

    @Mock
    private UserWorkspaceService userWorkspaceService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    private User user;
    private Workspace workspace;

    @BeforeEach
    void setUp() {
        user = new User("abc", "abc@gmail.com", "abcd1234", UserRole.ADMIN);
        workspace = new Workspace("title", "desc", user);
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(workspace, "workspaceId", 1L);
    }

    @Test
    @DisplayName("getAllWorkspaces: 워크스페이스가 존재하지 않을 때")
    void getAllWorkspaces_WhenNotExist() {
        when(workspaceRepository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> workspaceService.getAllWorkspace())
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("getAllWorkspaces: 워크스페이스가 존재할 때")
    void getAllWorkspaces_WhenExist() {
        when(workspaceRepository.findAll()).thenReturn(List.of(workspace));

        List<WorkspaceFindResponseDto> dto = workspaceService.getAllWorkspace();

        assertNotNull(dto);
        assertThat(dto.get(0).getId()).isEqualTo(workspace.getWorkspaceId());
        assertThat(dto.get(0).getName()).isEqualTo(workspace.getName());
    }

    @Test
    @DisplayName("updateWorkspaces: 워크스페이스가 존재하지 않을 때")
    void updateWorkspace_WhenNotExistId() {
        Long workspaceId = 1L;
        String name = "수정된 제목";
        String description = "수정된 내용";
        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto(name, description);

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workspaceService.updateWorkspace(workspaceId, workspaceRequestDto))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("updateWorkspaces: 워크스페이스가 존재할 때")
    void updateWorkspace_WhenExistId() {
        Long workspaceId = 1L;
        String name = "수정된 제목";
        String description = "수정된 내용";
        Workspace preworkspace = new Workspace("title", "desc", user);
        Workspace postworkspace = new Workspace("title", "desc", user);
        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto(name, description);

        ReflectionTestUtils.setField(postworkspace, "workspaceId", 1L);
        postworkspace.updateWorkspace(name, description);

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(preworkspace));
        when(workspaceRepository.save(preworkspace)).thenReturn(postworkspace);

        WorkspaceResponseDto workspaceResponseDto = workspaceService.updateWorkspace(workspaceId, workspaceRequestDto);

        assertNotNull(workspaceResponseDto);
        assertThat(preworkspace.getName()).isEqualTo(name);
        assertThat(preworkspace.getDescription()).isEqualTo(description);
    }

    @Test
    void inviteAcceptWorkspace_WhenExist() {
        MemberRole memberRole = MemberRole.MEMBER;
        WorkspaceInviteAcceptRequestDto requestDto = new WorkspaceInviteAcceptRequestDto("ACCEPTED");
        UserWorkspace newUserWorkspace = new UserWorkspace("INVITE", memberRole, user, workspace);
        ReflectionTestUtils.setField(newUserWorkspace, "id", 1L);

        when(workspaceRepository.findById(workspace.getWorkspaceId())).thenReturn(Optional.of(workspace));
        when(userWorkspaceRepository.findById(newUserWorkspace.getId())).thenReturn(Optional.of(newUserWorkspace));
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(user);
        when(userWorkspaceRepository.save(any())).thenReturn(newUserWorkspace);

        WorkspaceInviteResponseDto dto = workspaceService.inviteAcceptWorkspace(workspace.getWorkspaceId(), user.getId(), authentication, requestDto);

        assertNotNull(dto);
        assertThat(newUserWorkspace.getInvitationStatus()).isEqualTo("ACCEPTED");
    }
}