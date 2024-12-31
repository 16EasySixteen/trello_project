package com.example.trelloproject.board.entity;

import com.example.trelloproject.S3.Image;
import com.example.trelloproject.board.dto.BoardCreateRequestDto;
import com.example.trelloproject.common.entity.CreateAndUpdateDateEntity;
import com.example.trelloproject.list.entity.BoardList;
import com.example.trelloproject.workspace.entity.Workspace;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "board")
public class Board extends CreateAndUpdateDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardList> boardLists = new ArrayList<>();

    public Board() {}

    public Board(String title,  Workspace workspace) {
        this.title = title;
        this.workspace = workspace;
    }

    public void updateBoard(BoardCreateRequestDto boardCreateRequestDto) {
        this.title = boardCreateRequestDto.getTitle();
    }

    public void addImage(Image image) {
        images.add(image);
        image.associateWithBoard(this);
    }

    public void clearImages() {
        if (images != null) {
            images.clear();
        }
    }
}
