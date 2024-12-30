package com.example.trelloproject.workspace.service;

import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.entity.UserWorkspace;
import com.example.trelloproject.user.enumclass.MemberRole;
import com.example.trelloproject.user.repository.UserRepository;
import com.example.trelloproject.workspace.entity.Workspace;
import com.example.trelloproject.workspace.repository.UserWorkspaceRepository;
import com.example.trelloproject.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserWorkspaceServiceImpl implements UserWorkspaceService {

    //TODO : 코드 리팩토링
    private final UserRepository userRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final WorkspaceRepository workspaceRepository;

    @Override
    public void createUserWorkspace(Long userId, Long workspaceId, String invitationStatus, MemberRole memberRole) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace not found"));

        UserWorkspace userWorkspace = new UserWorkspace(invitationStatus, memberRole, user, workspace);
        userWorkspaceRepository.save(userWorkspace);
    }
}
