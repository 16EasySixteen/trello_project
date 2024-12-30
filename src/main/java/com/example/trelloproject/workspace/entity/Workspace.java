package com.example.trelloproject.workspace.entity;

import com.example.trelloproject.board.entity.Board;
import com.example.trelloproject.common.entity.CreateAndUpdateDateEntity;
import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.entity.UserWorkspace;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name= "workspace")
public class Workspace extends CreateAndUpdateDateEntity {
    @Id
    @Column(name = "workspace_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workspaceId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<UserWorkspace> userWorkspaces;

    public Workspace(String name, String description,User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public Workspace() {

    }

    public void updateWorkspace(String name, String description){
        this.name = name;
        this.description = description;
    }
}
