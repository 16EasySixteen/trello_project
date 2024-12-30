package com.example.trelloproject.card.controller;

import com.example.trelloproject.card.dto.addManager.AddManagerRequestDto;
import com.example.trelloproject.card.dto.addManager.AddManagerResponseDto;
import com.example.trelloproject.card.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards/{cardId}/managers")
public class AddManagerController {

    private ManagerService managerService;

    public AddManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<AddManagerResponseDto> CreateManager(
            @PathVariable Long cardId,
            @PathVariable Long userId,
            @RequestPart(value = "addManagerRequestDto" ,required = false) AddManagerRequestDto addManagerRequestDto
    ) {
        return new ResponseEntity<>(managerService.createManager(cardId, userId, addManagerRequestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{managerId}")
    public ResponseEntity<Void> DeleteManager(
            @PathVariable Long managerId
    ) {
        managerService.deleteManager(managerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
