package com.example.trelloproject.card.repository;

import com.example.trelloproject.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CardRepository extends JpaRepository<Card, Long>, CardCustomRepository{

}
