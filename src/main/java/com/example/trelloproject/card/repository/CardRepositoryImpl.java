package com.example.trelloproject.card.repository;


import com.example.trelloproject.board.entity.QBoard;
import com.example.trelloproject.card.entity.Card;
import com.example.trelloproject.card.entity.QCard;
import com.example.trelloproject.card.entity.QManager;
import com.example.trelloproject.list.entity.QBoardList;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class CardRepositoryImpl implements CardCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CardRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Card> searchCard(String title, String description, LocalDateTime endAt, String managerName, Long boardId) {

        QCard card = QCard.card;
        QManager manager = QManager.manager;
        QBoardList boardList = QBoardList.boardList;

        return jpaQueryFactory
                .selectFrom(card)
                .leftJoin(card.managers, manager).fetchJoin()
                .join(card.boardList, boardList).fetchJoin()
                .where(
                        titleEq(title),
                        descriptionEq(description),
                        endAtEq(endAt),
                        boardIdEq(boardId),
                        managerNameEq(managerName)
                )
                .fetch();
    }
    private BooleanExpression titleEq(String title) {
        return title != null ? QCard.card.title.eq(title) : null;
    }

    private BooleanExpression descriptionEq(String description) {
        return description != null ? QCard.card.description.eq(description) : null;
    }

    private BooleanExpression endAtEq(LocalDateTime endAt) {
        return endAt != null ? QCard.card.endAt.eq(endAt) : null;
    }

    private BooleanExpression managerNameEq(String managerName) {
        return managerName != null ? QManager.manager.managerName.eq(managerName) : null;
    }

    private BooleanExpression boardIdEq(Long boardId) {
        return boardId != null ? QBoard.board.id.eq(boardId) : null;
    }
}
