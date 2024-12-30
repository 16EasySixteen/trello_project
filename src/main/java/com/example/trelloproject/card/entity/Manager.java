package com.example.trelloproject.card.entity;

import com.example.trelloproject.common.entity.CreateAndUpdateDateEntity;
import com.example.trelloproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="manager")
public class Manager extends CreateAndUpdateDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String managerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;


    public Manager() {
    }

    public Manager(User user, Card card) {
        this.user = user;
        this.card = card;
        this.managerName = user.getName();
    }

}
