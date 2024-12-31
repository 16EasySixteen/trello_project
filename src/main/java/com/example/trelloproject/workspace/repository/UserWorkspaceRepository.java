package com.example.trelloproject.workspace.repository;

import com.example.trelloproject.user.entity.UserWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, Long> {
}
