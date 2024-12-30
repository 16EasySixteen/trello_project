package com.example.trelloproject.card.repository;

import com.example.trelloproject.card.entity.Card;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CardCustomRepository {

    List<Card> searchCard (String title, String description, LocalDateTime endAt, String name, Long boardId);
}
