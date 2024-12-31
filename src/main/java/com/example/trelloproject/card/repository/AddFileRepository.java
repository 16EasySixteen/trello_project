package com.example.trelloproject.card.repository;

import com.example.trelloproject.card.entity.AddFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddFileRepository extends JpaRepository<AddFile, Long> {

    List<AddFile> findAllByCardId(Long cardId);
}
