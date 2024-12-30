package com.example.trelloproject.card.repository;

import com.example.trelloproject.card.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
