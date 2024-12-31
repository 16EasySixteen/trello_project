package com.example.trelloproject.card.dto.addFile;

import lombok.Getter;

@Getter
public class AddFileRequestDto {

    private String savedFileName;

    private String size;

    private String filePath;

    public AddFileRequestDto(String savedFileName, String size, String filePath) {
        this.savedFileName = savedFileName;
        this.size = size;
        this.filePath = filePath;
    }
}
