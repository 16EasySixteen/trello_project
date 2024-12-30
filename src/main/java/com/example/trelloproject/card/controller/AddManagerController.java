package com.example.trelloproject.card.controller;

import com.example.trelloproject.card.dto.addManager.AddManagerRequestDto;
import com.example.trelloproject.card.dto.addManager.AddManagerResponseDto;
import com.example.trelloproject.card.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspaces/{workspaceId}/cards/{cardId}/managers")
public class AddManagerController {

    private ManagerService managerService;

    public AddManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping
    public ResponseEntity<AddManagerResponseDto> CreateManager(
            @PathVariable Long cardId,
            @RequestBody AddManagerRequestDto addManagerRequestDto
    ) {
        return new ResponseEntity<>(managerService.createManager(cardId, addManagerRequestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{managerId}")
    public ResponseEntity<Void> DeleteManager(
            @PathVariable Long managerId
    ) {
        managerService.deleteManager(managerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
