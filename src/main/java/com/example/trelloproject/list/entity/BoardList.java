package com.example.trelloproject.list.entity;

import com.example.trelloproject.board.entity.Board;
import com.example.trelloproject.card.entity.Card;
import com.example.trelloproject.common.entity.CreateAndUpdateDateEntity;
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
@Table(name = "list")
public class BoardList extends CreateAndUpdateDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "boardList", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    public BoardList() {}

    public BoardList(String title, Integer position, Board board) {
        this.title = title;
        this.position = position;
        this.board = board;
    }

    public void updatePosition(String title, Integer position) {
        this.title = title;
        this.position = position;
    }

    public void downPosition() {
        this.position--;
    }

    public void upPosition() {
        this.position++;
    }
}
