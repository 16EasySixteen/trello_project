package com.example.trelloproject.comment.entity;

import com.example.trelloproject.card.entity.Card;
import com.example.trelloproject.common.entity.CreateAndUpdateDateEntity;
import com.example.trelloproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="comment")
public class Comment extends CreateAndUpdateDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long CommentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Comment(String content, User user, Card card) {
        this.content = content;
        this.user = user;
        this.card = card;
    }

    public Comment() {

    }

    public void updateComment(String content){
        this.content = content;
    }
}
