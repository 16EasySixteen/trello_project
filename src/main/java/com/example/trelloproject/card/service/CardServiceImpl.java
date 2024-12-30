package com.example.trelloproject.card.service;

import com.example.trelloproject.card.dto.cardCreate.CardCreateRequestDto;
import com.example.trelloproject.card.dto.cardCreate.CardCreateResponseDto;
import com.example.trelloproject.card.dto.cardFind.CardFindOneResponseDto;
import com.example.trelloproject.card.dto.cardFind.CardFindResponseDto;
import com.example.trelloproject.card.dto.cardFind.CardSearchResponseDto;
import com.example.trelloproject.card.dto.cardUpdate.CardUpdateRequestDto;
import com.example.trelloproject.card.dto.cardUpdate.CardUpdateResponseDto;
import com.example.trelloproject.card.entity.Card;
import com.example.trelloproject.card.repository.CardRepository;
import com.example.trelloproject.emoji.dto.EmojiFindResponseDto;
import com.example.trelloproject.list.entity.BoardList;
import com.example.trelloproject.list.repository.ListRepository;
import com.example.trelloproject.user.repository.UserRepository;
import com.example.trelloproject.comment.dto.CommentFindResponseDto;
import com.example.trelloproject.comment.entity.Comment;
import com.example.trelloproject.comment.repository.CommentRepository;
import com.example.trelloproject.emoji.entity.Emoji;
import com.example.trelloproject.emoji.repository.EmojiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EmojiRepository emojiRepository;


    @Override
    @Transactional
    public CardCreateResponseDto createCard(Long listId, CardCreateRequestDto cardCreateRequestDto) {

        BoardList boardList = listRepository.findById(listId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        Card savedCard = cardRepository.save(new Card(
                cardCreateRequestDto.getTitle(),
                cardCreateRequestDto.getDescription(),
                cardCreateRequestDto.getEndAt(),
                boardList)
        );


        return new CardCreateResponseDto(savedCard.getId(),
                savedCard.getTitle(),
                savedCard.getDescription(),
                savedCard.getEndAt(),
                savedCard.getCreatedAt(),
                savedCard.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public CardUpdateResponseDto updateCard(Long cardId, CardUpdateRequestDto cardUpdateRequestDto) {

        Card findCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card doesn't exist"));

        findCard.setTitle(cardUpdateRequestDto.getNewTitle());
        findCard.setDescription(cardUpdateRequestDto.getNewDescription());
        findCard.setEndAt(cardUpdateRequestDto.getNewEndAt());

        Card updatedCard = cardRepository.save(findCard);
        return new CardUpdateResponseDto(
                updatedCard.getId(),
                updatedCard.getTitle(),
                updatedCard.getDescription(),
                updatedCard.getEndAt(),
                updatedCard.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public CardFindOneResponseDto getCard(Long cardId) {

        Card findCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card doesn't exist"));


        List<Comment> allComments = commentRepository.findByCard(findCard);
        List<CommentFindResponseDto> comments = new ArrayList<>();
        for (Comment comment : allComments) {
            // 이모티콘 조회
            List<Emoji> commentEmojis = emojiRepository.findByComment(comment);
            List<EmojiFindResponseDto> emojisDto = commentEmojis.stream().map(EmojiFindResponseDto::new).toList();

            CommentFindResponseDto commentFindResponseDto = new CommentFindResponseDto(
                    comment.getCommentId(),
                    comment.getUser().getId(),
                    comment.getContent(),
                    emojisDto
            );
            comments.add(commentFindResponseDto);
        }

        return new CardFindOneResponseDto(findCard.getId(),
                findCard.getTitle(),
                findCard.getDescription(),
                findCard.getEndAt(),
                comments);

    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        //카드 조회
        Card findCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card doesn't exist"));

        //카드 삭제
        cardRepository.deleteById(cardId);

    }

    @Override
    @Transactional
    public List<CardSearchResponseDto> searchAndConvertCard(String title, String description, LocalDateTime endAt, String name, Long boardId) {
        List<Card> cards = searchCard(title, description, endAt, name, boardId) ;
        return converToDto(cards);
    }

    private List<CardSearchResponseDto> converToDto(List<Card> cards) {
        return cards.stream()
                .map(card -> new CardSearchResponseDto(
                        card.getId(),
                        card.getTitle()
                ))
                .toList();
    }


    @Override
    public List<Card> searchCard(String title, String description, LocalDateTime endAt, String name, Long boardId) {
        return cardRepository.searchCard(title, description, endAt, name, boardId);
    }

}
