package com.example.trelloproject.S3;

import com.example.trelloproject.board.entity.Board;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath; // S3 경로
    private String fileName; // 파일 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public Image() {}

    public Image(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public static Image of(String filePath, String fileName) {
        return new Image(filePath, fileName);
    }

    public void associateWithBoard(Board board) {
        this.board = board;
    }
}
