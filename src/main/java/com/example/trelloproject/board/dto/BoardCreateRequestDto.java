package com.example.trelloproject.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class BoardCreateRequestDto {

    @NotBlank(message = "보드 제목을 입력해주세요.")
    private final String title;
    private final MultipartFile image;

    public BoardCreateRequestDto(String title, MultipartFile image) {
        this.title = title;
        this.image = image;
    }
}
