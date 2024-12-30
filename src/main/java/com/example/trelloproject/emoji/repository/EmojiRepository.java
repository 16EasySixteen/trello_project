package com.example.trelloproject.emoji.repository;

import com.example.trelloproject.comment.entity.Comment;
import com.example.trelloproject.emoji.entity.Emoji;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmojiRepository extends JpaRepository<Emoji, Long> {
    List<Emoji> findByComment(Comment comment);
}