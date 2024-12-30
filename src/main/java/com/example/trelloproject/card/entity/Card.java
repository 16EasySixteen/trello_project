package com.example.trelloproject.card.entity;


import com.example.trelloproject.common.entity.CreateAndUpdateDateEntity;
import com.example.trelloproject.list.entity.BoardList;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table
public class Card extends CreateAndUpdateDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private LocalDateTime endAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private BoardList boardList;


    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<AddFile> fileList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Manager> managers = new ArrayList<>();


//    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    private List<Comment> comments;

    public Card(){}

    public Card(String title, String description, LocalDateTime endAt, BoardList boardList) {
        this.title = title;
        this.description = description;
        this.endAt = endAt;
        this.boardList = boardList;
    }


    public void setTitle(String title) {
        this.title =title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }
}
