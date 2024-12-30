package com.example.trelloproject.user.entity;

import com.example.trelloproject.user.enumclass.UserRole;
import com.example.trelloproject.user.enumclass.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private UserStatus userStatus;

    @Column(nullable = false)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserWorkspace> userWorkspace = new ArrayList<>();

    public User() {}

    public User(String name, String email, String password, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.userStatus = UserStatus.NORMAL;
    }

    public void updateUserStatus() {
        this.userStatus = UserStatus.DELETED;
    }
}

