package com.example.trelloproject.comment.repository;

import com.example.trelloproject.card.entity.Card;
import com.example.trelloproject.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCard(Card findCard);
}
