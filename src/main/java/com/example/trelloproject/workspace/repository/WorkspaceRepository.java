package com.example.trelloproject.workspace.repository;

import com.example.trelloproject.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @Query("SELECT w FROM Workspace w " +
            "LEFT JOIN w.userWorkspaces uw " +
            "WHERE (uw.user.id = :userId " +
            "AND (uw.invitationStatus = 'ACCEPT' OR uw.invitationStatus = 'ADMIN')) " +
            "OR w.user.id = :userId")
    List<Workspace> findByUserId(@Param("userId")Long userId);
}
