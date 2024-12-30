package com.example.trelloproject.card.controller;

import com.example.trelloproject.card.dto.cardCreate.CardCreateRequestDto;
import com.example.trelloproject.card.dto.cardCreate.CardCreateResponseDto;
import com.example.trelloproject.card.dto.cardFind.CardFindOneResponseDto;
import com.example.trelloproject.card.dto.cardFind.CardFindResponseDto;
import com.example.trelloproject.card.dto.cardFind.CardSearchResponseDto;
import com.example.trelloproject.card.dto.cardUpdate.CardUpdateRequestDto;
import com.example.trelloproject.card.dto.cardUpdate.CardUpdateResponseDto;
import com.example.trelloproject.card.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // 카드 생성
    @PostMapping("/lists/{listId}/cards")
    public ResponseEntity<CardCreateResponseDto> createCard(
            @PathVariable Long listId,
            @RequestBody CardCreateRequestDto cardCreateRequestDto
    ) {
        return new ResponseEntity<>(cardService.createCard(listId, cardCreateRequestDto), HttpStatus.CREATED);
    }

    // 카드 수정
    @PatchMapping("/cards/{cardId}")
    public ResponseEntity<CardUpdateResponseDto> updateCard(
            @PathVariable Long cardId,
            @RequestBody CardUpdateRequestDto cardUpdateRequestDto
    ) {
        return new ResponseEntity<>(cardService.updateCard(cardId, cardUpdateRequestDto),HttpStatus.OK);
    }

    // 카드 단건 조회
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<CardFindOneResponseDto> findCard(
            @PathVariable Long cardId
    ) {
        return new ResponseEntity<>(cardService.getCard(cardId), HttpStatus.OK);
    }

    // 카드 삭제
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(

            @PathVariable Long cardId
    ){
        cardService.deleteCard(cardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //카드 검색
    @GetMapping("/cards")
    public ResponseEntity<List<CardSearchResponseDto>> searchCards(
            @RequestParam (required = false) String title,
            @RequestParam (required = false) String description,
            @RequestParam (required = false) LocalDateTime endAt,
            @RequestParam (required = false) String managerName,
            @RequestParam (required = false) Long boardId){
        List<CardSearchResponseDto> findCards = cardService.searchAndConvertCard(title, description, endAt, managerName, boardId);
        return new ResponseEntity<>(findCards, HttpStatus.OK);
    }

}
