package com.example.trelloproject.card.dto.addManager;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AddManagerRequestDto {
    private Long userId;


    @JsonCreator
    public AddManagerRequestDto(@JsonProperty("userId") Long userId) {
        this.userId = userId;
    }
}
