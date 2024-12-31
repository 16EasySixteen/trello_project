package com.example.trelloproject.user.entity;

import com.example.trelloproject.user.enumclass.MemberRole;
import com.example.trelloproject.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "user_workspace")
public class UserWorkspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String invitationStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    // 초대 당한 사람
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    public UserWorkspace() {}

    public UserWorkspace(String invitationStatus, MemberRole memberRole, User user, Workspace workspace) {
        this.invitationStatus = invitationStatus;
        this.memberRole = memberRole;
        this.user = user;
        this.workspace = workspace;
    }

    public void updateStatus(String status){
        this.invitationStatus = status;
    }
}
