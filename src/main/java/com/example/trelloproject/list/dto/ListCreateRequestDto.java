package com.example.trelloproject.list.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ListCreateRequestDto {

    private String title;

    public ListCreateRequestDto(String title) {
        this.title = title;
    }
}
