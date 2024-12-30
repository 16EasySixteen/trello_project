package com.example.trelloproject.emoji.entity;

import com.example.trelloproject.comment.entity.Comment;
import com.example.trelloproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "emoji")
public class Emoji {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emoji_id")
    private Long EmojiId;

    private String symbol;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Emoji(String symbol, User user, Comment comment) {
        this.symbol = symbol;
        this.user = user;
        this.comment = comment;
    }

    public Emoji() {

    }
}
