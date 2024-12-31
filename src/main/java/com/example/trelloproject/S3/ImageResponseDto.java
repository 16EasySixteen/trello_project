package com.example.trelloproject.S3;

import lombok.Getter;

@Getter
public class ImageResponseDto {

    private Long id;
    private String filePath; // S3 경로
    private String fileName; // 파일 이름

    public ImageResponseDto(Long id, String filePath, String fileName) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public ImageResponseDto() {}

    public static ImageResponseDto toDto(Image image) {
        return new ImageResponseDto(
                image.getId(),
                image.getFilePath(),
                image.getFileName()
        );
    }
}
