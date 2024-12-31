package com.example.trelloproject.workspace.service;

import com.example.trelloproject.user.enumclass.MemberRole;

public interface UserWorkspaceService {

    void createUserWorkspace(Long userId, Long workspaceId, String invitationStatus, MemberRole memberRole);
}
