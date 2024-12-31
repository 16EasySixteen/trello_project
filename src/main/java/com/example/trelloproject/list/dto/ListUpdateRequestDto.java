package com.example.trelloproject.list.dto;

import lombok.Getter;

@Getter
public class ListUpdateRequestDto {

    private final String title;
    private final Integer position;

    public ListUpdateRequestDto(String title, Integer position) {
        this.title = title;
        this.position = position;
    }
}
